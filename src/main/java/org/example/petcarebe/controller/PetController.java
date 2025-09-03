package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.pet.CreatePetRequest;
import org.example.petcarebe.dto.request.pet.UpdatePetRequest;
import org.example.petcarebe.dto.response.PetResponse;
import org.example.petcarebe.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@RequestBody CreatePetRequest request) {
        PetResponse createdPet = petService.createPet(request);
        return ResponseEntity.ok(createdPet);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PetResponse>> getPetsByCustomerId(@PathVariable Long customerId) {
        List<PetResponse> pets = petService.getPetsByCustomerId(customerId);
        return ResponseEntity.ok(pets);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long petId, @RequestBody UpdatePetRequest request) {
        PetResponse updatedPet = petService.updatePet(petId, request);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }
}

