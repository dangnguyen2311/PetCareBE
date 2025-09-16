package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.pet.CreatePetRequest;
import org.example.petcarebe.dto.request.pet.CreatePetWeightRecordRequest;
import org.example.petcarebe.dto.request.pet.UpdatePetRequest;
import org.example.petcarebe.dto.response.pet.PetResponse;
import org.example.petcarebe.dto.response.pet.PetWeightRecordResponse;
import org.example.petcarebe.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@RequestBody CreatePetRequest request) {
        try {
            PetResponse createdPet = petService.createPet(request);
            return ResponseEntity.ok(createdPet);
        } catch (Exception e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            PetResponse error = new  PetResponse();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PetResponse>> getPetsByCustomerId(@PathVariable Long customerId) {
        List<PetResponse> pets = petService.getPetsByCustomerId(customerId);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PetResponse>> getPetsByClientId(@PathVariable String clientId) {
        try {
            List<PetResponse> petResponseList = petService.getPetsByClientId(clientId);
            return ResponseEntity.ok(petResponseList);
        } catch (RuntimeException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/getlist")
    public ResponseEntity<List<PetResponse>> getAllPets() {
        try {
            List<PetResponse> pets = petService.getAllPets();
            return ResponseEntity.ok(pets);
        } catch (RuntimeException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long petId, @RequestBody UpdatePetRequest request) {
        PetResponse updatedPet = petService.updatePet(petId, request);
        return ResponseEntity.ok(updatedPet);
    }

    @PostMapping("/{petId}/image")
    public ResponseEntity<PetResponse> updatePetImage(@PathVariable Long petId, @RequestParam("file") MultipartFile file) {
        PetResponse updatedPet = petService.updatePetImage(petId, file);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{petId}/add-weight")
    public ResponseEntity<PetWeightRecordResponse> addPetWeightRecordByPetId(
            @PathVariable Long petId,
            CreatePetWeightRecordRequest request
    ) {
        PetWeightRecordResponse response = petService.createPetWeightRecord(petId, request);
        return ResponseEntity.ok(response);
    }


}

