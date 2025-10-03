package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.pet.*;

import org.example.petcarebe.dto.response.activity.DailyActivityResponse;
import org.example.petcarebe.dto.response.pet.PetFoodRecordResponse;
import org.example.petcarebe.dto.response.pet.PetImageRecordResponse;
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
import java.util.ArrayList;
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

    @Autowired
    private PetFoodRecordRepository petFoodRecordRepository;

    @Autowired
    private PetImageRecordRepository petImageRecordRepository;

    @Transactional
    public PetResponse createPet(CreatePetRequest request) {
        System.out.println("createPet::::::: " +  request.getClientId());
        Optional<Customer> customerOptional = customerRepository.findByClientId(request.getClientId());
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Customer customer;
        if(customerOptional.isEmpty()){
            customer = new Customer();
            customer.setClientId(request.getClientId());
            customer.setStatus("ACTIVE");
            customer.setCreatedDate(LocalDate.now());
            customerRepository.save(customer);
        }
        else customer = customerOptional.get();

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
        Pet savedPet = petRepository.save(pet);

        /**
        * REMEMBER TO SAVE FIRST
        */

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setCreatedDate(LocalDate.now());
        medicalRecord.setPet(savedPet);

        medicalRecordRepository.save(medicalRecord);
        return convertToResponse(savedPet);
    }

    public List<PetResponse> getPetsByCustomerId(Long customerId) {
        return petRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public List<PetResponse> getPetsByClientId(String clientId) {
        return petRepository.findByCustomer_ClientId(clientId).stream()
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
        MedicalRecord medicalRecord = medicalRecordRepository.findByPet_Id(petId);
        medicalRecordRepository.delete(medicalRecord);
        petRepository.deletePet(petId);
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
        petWeightRecord.setMedicalRecord(medicalRecordOptional.get());
        petWeightRecord.setRecordDate(request.getRecordDate());
        System.out.println("thong tin" + request.getRecordDate() + " " + request.getWeight());
        petWeightRecord.setNotes(request.getNotes());

        PetWeightRecord savePetWeightRecord = petWeightRecordRepository.save(petWeightRecord);
        return convertRecordToResponse(savePetWeightRecord, "PetWeightRecord created");

    }
    public PetResponse getPetById(Long petId) {
        return convertToResponse(petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found")));
    }
    public PetFoodRecordResponse createPetFoodRecord(Long petId, CreatePetFoodRecordRequest request) {
        Pet pet =  petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(pet)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));

        PetFoodRecord petFoodRecord = new PetFoodRecord();
        petFoodRecord.setFoodType(request.getFoodType());
        petFoodRecord.setAmountFood(request.getAmountFood());
        petFoodRecord.setRecordDate(request.getRecordDate());
        petFoodRecord.setNotes(request.getNotes());
        petFoodRecord.setMedicalRecord(medicalRecordByPet);

        PetFoodRecord savePetFoodRecord = petFoodRecordRepository.save(petFoodRecord);
        return convertRecordToResponse(savePetFoodRecord, "PetFoodRecord created");

    }

    public PetImageRecordResponse createPetImageRecord(Long petId, CreatePetImageRecordRequest request){
        Pet pet =  petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(pet)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));

        PetImageRecord petImageRecord = new PetImageRecord();
        petImageRecord.setMedicalRecord( medicalRecordByPet);
        petImageRecord.setRecordDate(request.getRecordDate());
        petImageRecord.setNotes(request.getNotes());
        petImageRecord.setImgUrl(request.getImgUrl());

        PetImageRecord savedPetImageRecord = petImageRecordRepository.save(petImageRecord);
        return convertRecordToResponse(savedPetImageRecord, "PetImageRecord created");
    }

    public List<PetFoodRecordResponse> getPetFoodRecordsByPetId(Long petId) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetFoodRecord> petFoodRecordList = petFoodRecordRepository.findAllByMedicalRecord(medicalRecordByPet, Sort.by(Sort.Direction.DESC, "recordDate"));
        return petFoodRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }
    public List<PetFoodRecordResponse> getPetFoodRecordsByPetIdAndDate(Long petId, LocalDate date) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetFoodRecord> petFoodRecordList;
        if (date != null) {
            // Lọc theo ngày
            petFoodRecordList = petFoodRecordRepository
                    .findAllByMedicalRecordAndRecordDate(
                            medicalRecordByPet,
                            date,
                            Sort.by(Sort.Direction.DESC, "recordDate")
                    );
        } else {
            // Lấy tất cả bản ghi
            petFoodRecordList = petFoodRecordRepository
                    .findAllByMedicalRecord(
                            medicalRecordByPet,
                            Sort.by(Sort.Direction.DESC, "id")
                    );
        }
        return petFoodRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }

    public List<PetWeightRecordResponse> getPetWeightRecordsByPetIdAndDate(Long petId, LocalDate date) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetWeightRecord> petWeightRecordList;
        if (date != null) {
            // Lọc theo ngày
            petWeightRecordList = petWeightRecordRepository
                    .findAllByMedicalRecordAndRecordDate(
                            medicalRecordByPet,
                            date,
                            Sort.by(Sort.Direction.DESC, "recordDate")
                    );
        } else {
            // Lấy tất cả bản ghi
            petWeightRecordList = petWeightRecordRepository
                    .findAllByMedicalRecord(
                            medicalRecordByPet,
                            Sort.by(Sort.Direction.DESC, "id")
                    );
        }
        return petWeightRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }
    public List<PetImageRecordResponse> getPetImageRecordsByPetIdAndDate(Long petId, LocalDate date) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetImageRecord> petImageRecordList;
        if (date != null) {
            // Lọc theo ngày
            petImageRecordList = petImageRecordRepository
                    .findAllByMedicalRecordAndRecordDate(
                            medicalRecordByPet,
                            date,
                            Sort.by(Sort.Direction.DESC, "recordDate")
                    );
        } else {
            // Lấy tất cả bản ghi
            petImageRecordList = petImageRecordRepository
                    .findAllByMedicalRecord(
                            medicalRecordByPet,
                            Sort.by(Sort.Direction.DESC, "id")
                    );
        }
        return petImageRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }

    public List<PetWeightRecordResponse> getPetWeightRecordsByPetId(Long petId) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetWeightRecord> petWeightRecordList = petWeightRecordRepository.findAllByMedicalRecord(medicalRecordByPet, Sort.by(Sort.Direction.DESC, "recordDate"));
        return petWeightRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }

    public List<PetImageRecordResponse> getPetImageRecordsByPetId(Long petId) {
        Pet petById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        MedicalRecord medicalRecordByPet = medicalRecordRepository.findByPet(petById)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
        List<PetImageRecord> petImageRecordList = petImageRecordRepository.findAllByMedicalRecord(medicalRecordByPet, Sort.by(Sort.Direction.DESC, "recordDate"));
        return petImageRecordList.stream()
                .map(this::convertRecordToResponse)
                .toList();
    }



    private PetImageRecordResponse convertRecordToResponse(PetImageRecord petImageRecord) {
        return new PetImageRecordResponse(
                petImageRecord.getId(),
                petImageRecord.getRecordDate(),
                petImageRecord.getImgUrl(),
                petImageRecord.getNotes(),
                ""
        );
    }
    private PetImageRecordResponse convertRecordToResponse(PetImageRecord petImageRecord, String message) {
        return new PetImageRecordResponse(
                petImageRecord.getId(),
                petImageRecord.getRecordDate(),
                petImageRecord.getImgUrl(),
                petImageRecord.getNotes(),
                message
        );
    }
    private PetWeightRecordResponse convertRecordToResponse(PetWeightRecord record, String message) {
        return new PetWeightRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getWeight(),
                record.getNotes(),
                message

        );

    }
    private PetWeightRecordResponse convertRecordToResponse(PetWeightRecord record) {
        return new PetWeightRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getWeight(),
                record.getNotes(),
                ""

        );

    }
    private PetFoodRecordResponse convertRecordToResponse(PetFoodRecord record) {
        return new PetFoodRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getFoodType(),
                record.getAmountFood(),
                record.getNotes(),
                ""
        );
    }
    private PetFoodRecordResponse convertRecordToResponse(PetFoodRecord record, String message) {
        return new PetFoodRecordResponse(
                record.getId(),
                record.getRecordDate(),
                record.getFoodType(),
                record.getAmountFood(),
                record.getNotes(),
                message
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


    public DailyActivityResponse getDailyActivities(Long petId, LocalDate date) {
        // Bổ sung sau
        return new DailyActivityResponse();
    }

    public List<DailyActivityResponse> getActivitiesInRange(Long petId, LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>();
    }

}

