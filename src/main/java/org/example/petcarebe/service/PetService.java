package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.pet.CreatePetRequest;
import org.example.petcarebe.dto.request.pet.UpdatePetRequest;

import org.example.petcarebe.dto.response.PetResponse;
import org.example.petcarebe.model.AnimalType;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.Pet;
import org.example.petcarebe.repository.AnimalTypeRepository;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    public PetResponse createPet(CreatePetRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        AnimalType animalType = animalTypeRepository.findById(request.getAnimalTypeId())
                .orElseThrow(() -> new RuntimeException("AnimalType not found"));

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setDatebirth(request.getDateOfBirth());
        pet.setColor(request.getColor());
        pet.setCreatedDate(LocalDate.now());
        pet.setAnimalType(animalType);
        pet.setCustomer(customer);

        Pet savedPet = petRepository.save(pet);
        return convertToResponse(savedPet);
    }

    public List<PetResponse> getPetsByCustomerId(Long customerId) {
        return petRepository.findByCustomerId(customerId).stream()
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

    public void deletePet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new RuntimeException("Pet not found");
        }
        petRepository.deleteById(petId);
    }

    private PetResponse convertToResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getGender(),
                pet.getDatebirth(),
                pet.getColor(),
                pet.getAnimalType().getId(),
                pet.getAnimalType().getName(),
                pet.getCustomer().getId()
        );
    }
}

