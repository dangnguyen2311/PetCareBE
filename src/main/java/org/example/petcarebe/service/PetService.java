package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.pet.CreatePetRequest;
import org.example.petcarebe.dto.request.pet.CreatePetWeightRecordRequest;
import org.example.petcarebe.dto.request.pet.UpdatePetRequest;

import org.example.petcarebe.dto.response.pet.PetResponse;
import org.example.petcarebe.dto.response.pet.PetWeightRecordResponse;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ImageUploadService imageUploadService;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PetWeightRecordRepository petWeightRecordRepository;

    public PetResponse createPet(CreatePetRequest request) {
        System.out.println("createPet::::::: " +  request.getCustomerId());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Optional<AnimalType> animalType = animalTypeRepository.findById(request.getAnimalTypeId());

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender().toUpperCase());
        pet.setDatebirth(request.getDateOfBirth());
        pet.setColor(request.getColor());
        pet.setCreatedDate(LocalDate.now());
        pet.setIsDeleted(false);
        pet.setAnimalType(animalType.orElse(null));
        pet.setCustomer(customer);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setCreatedDate(LocalDate.now());
        medicalRecord.setPet(pet);

        medicalRecordRepository.save(medicalRecord);
        Pet savedPet = petRepository.save(pet);

        return convertToResponse(savedPet);
    }

    public List<PetResponse> getPetsByCustomerId(Long customerId) {
        return petRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PetResponse> getAllPets() {
        return petRepository.findAllByIsDeleted(false, (Sort.by(Sort.Direction.ASC, "id"))).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

    }

    public PetResponse updatePet(Long petId, UpdatePetRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        AnimalType animalType = animalTypeRepository.findById(request.getAnimalTypeId())
                .orElseThrow(() -> new RuntimeException("AnimalType not found"));

        pet.setName(request.getName());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setDatebirth(request.getDateOfBirth());
        pet.setColor(request.getColor());
        pet.setAnimalType(animalType);

        Pet updatedPet = petRepository.save(pet);
        return convertToResponse(updatedPet);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new RuntimeException("Pet not found");
        }
        petRepository.deleteById(petId);
    }

    public PetResponse updatePetImage(Long petId, MultipartFile file) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        String imageUrl = imageUploadService.uploadImage(file);
        pet.setImgUrl(imageUrl);

        Pet updatedPet = petRepository.save(pet);
        return convertToResponse(updatedPet);
    }



    public PetResponse updateAnimalType(Long petId, UpdatePetRequest request) {
        Pet pet =  petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        AnimalType animalType = animalTypeRepository.findById(request.getAnimalTypeId())
                .orElseThrow(() -> new RuntimeException("AnimalType not found"));
        pet.setAnimalType(animalType);
        Pet updatedPet = petRepository.save(pet);
        return convertToResponse(updatedPet);

    }


    public PetWeightRecordResponse createPetWeightRecord(Long petId, CreatePetWeightRecordRequest request) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isEmpty()) {
            throw new RuntimeException("Pet is not exists");
        }
        Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findByPet(petOptional.get());
        if (medicalRecordOptional.isEmpty()) {
            throw new RuntimeException("MedicalRecord is not exists");
        }
        PetWeightRecord petWeightRecord = new PetWeightRecord();
        petWeightRecord.setWeight(request.getWeight());
        petWeightRecord.setMedicalRecord( medicalRecordOptional.get());
        petWeightRecord.setRecordDate(LocalDate.now());
        petWeightRecord.setNotes(request.getNotes());

        PetWeightRecord savePetWeightRecord = petWeightRecordRepository.save(petWeightRecord);
        return convertToResponse(savePetWeightRecord, "PetWeightRecord created");

    }
    private PetWeightRecordResponse convertToResponse(PetWeightRecord record, String message) {
        return new PetWeightRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getWeight(),
                record.getNotes(),
                message

        );

    }
    private PetWeightRecordResponse convertToResponse(PetWeightRecord record) {
        return new PetWeightRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getWeight(),
                record.getNotes(),
                ""

        );

    }

    private PetResponse convertToResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getImgUrl(),
                pet.getGender(),
                pet.getDatebirth(),
                pet.getColor(),
                pet.getAnimalType().getId(),
                pet.getAnimalType().getName(),
                pet.getCustomer().getId()

        );
    }
}

