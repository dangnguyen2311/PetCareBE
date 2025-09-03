package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.animaltype.UpdateAnimalTypeRequest;
import org.example.petcarebe.dto.request.animaltype.CreateAnimalTypeRequest;
import org.example.petcarebe.dto.response.AnimalTypeResponse;
import org.example.petcarebe.model.AnimalType;
import org.example.petcarebe.repository.AnimalTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AnimalTypeService {

    @Autowired
    private AnimalTypeRepository animalTypeRepository;

    public AnimalTypeResponse createAnimalType(CreateAnimalTypeRequest request) {
        AnimalType animalTypeExist = animalTypeRepository.findAnimalTypeByName(request.getName());
        if (animalTypeExist != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "AnimalType already exists");
        }
        AnimalType animalType = new AnimalType();
        animalType.setName(request.getName());
        animalType.setDescription(request.getDescription());

        AnimalType createdAnimalType = animalTypeRepository.save(animalType);
        return convertToResponse(createdAnimalType, "Created animal type with id " + createdAnimalType.getId());
    }

    public AnimalTypeResponse updateAnimalType(Long id, UpdateAnimalTypeRequest request) {
        AnimalType animalType = animalTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AnimalType not found with id: " + id));

        animalType.setName(request.getName());
        animalType.setDescription(request.getDescription());

        AnimalType updatedAnimalType = animalTypeRepository.save(animalType);
        return convertToResponse(updatedAnimalType, "Animal type updated successfully");
    }

    private AnimalTypeResponse convertToResponse(AnimalType animalType, String message) {
        return new AnimalTypeResponse(
                animalType.getId(),
                animalType.getName(),
                animalType.getDescription(),
                message
        );
    }

    public AnimalTypeResponse deleteAnimalType(Long id) {
        Optional<AnimalType> animalType = animalTypeRepository.findById(id);
        animalType.ifPresent(animalTypeToDelete -> {
            animalTypeToDelete.setIsDeleted(true);

        });
        return convertToResponse(animalType.get(), "Animal type deleted successfully");
    }
}

