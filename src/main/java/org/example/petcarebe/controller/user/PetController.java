package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.pet.*;
import org.example.petcarebe.dto.response.pet.PetFoodRecordResponse;
import org.example.petcarebe.dto.response.pet.PetImageRecordResponse;
import org.example.petcarebe.dto.response.pet.PetResponse;
import org.example.petcarebe.dto.response.pet.PetWeightRecordResponse;
import org.example.petcarebe.service.PetService;
import org.example.petcarebe.dto.response.activity.DailyActivityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long petId) { // @RequestParam("petId") Long petId,
        try{
            PetResponse response = petService.getPetById(petId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            PetResponse error = new  PetResponse();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            PetResponse error = new  PetResponse();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PetResponse>> getPetsByCustomerId(@PathVariable Long customerId) {
        try {
            List<PetResponse> pets = petService.getPetsByCustomerId(customerId);
            return ResponseEntity.ok(pets);
        } catch (RuntimeException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
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
        } catch (Exception e) {
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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
        catch (Exception e) {
            List<PetResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long petId, @RequestBody UpdatePetRequest request) {
        try {
            PetResponse updatedPet = petService.updatePet(petId, request);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            PetResponse error = new PetResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e) {
            PetResponse error = new PetResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{petId}/image")
    public ResponseEntity<PetResponse> updatePetImage(@PathVariable Long petId, @RequestParam("file") MultipartFile file) {
        try {
            PetResponse updatedPet = petService.updatePetImage(petId, file);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            PetResponse error = new PetResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e) {
            PetResponse error = new PetResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        try {
            petService.deletePet(petId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{petId}/add-weight")
    public ResponseEntity<PetWeightRecordResponse> addPetWeightRecordByPetId(
            @PathVariable Long petId,
            @RequestBody  CreatePetWeightRecordRequest request
    ) {
        try {
            PetWeightRecordResponse response = petService.createPetWeightRecord(petId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            PetWeightRecordResponse error = new  PetWeightRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            PetWeightRecordResponse error = new  PetWeightRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{petId}/add-food")
    public ResponseEntity<PetFoodRecordResponse> addPetFoodRecordByPetId(
            @PathVariable Long petId,
            @RequestBody CreatePetFoodRecordRequest request){
        try{
            PetFoodRecordResponse response = petService.createPetFoodRecord(petId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            PetFoodRecordResponse error = new  PetFoodRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            PetFoodRecordResponse error = new  PetFoodRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{petId}/add-image")
    public ResponseEntity<PetImageRecordResponse> addPetImageRecordByPetId(
            @PathVariable Long petId,
            @RequestBody CreatePetImageRecordRequest request) {
        try{
            PetImageRecordResponse response = petService.createPetImageRecord(petId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            PetImageRecordResponse error = new  PetImageRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            PetImageRecordResponse error = new  PetImageRecordResponse();
            error.setMessage("Error creating : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

//    @GetMapping("/{petId}/food-records")
//    public ResponseEntity<List<PetFoodRecordResponse>> findPetFoodRecordsByPetId(@PathVariable Long petId) {
//        try{
//            List<PetFoodRecordResponse> responses = petService.getPetFoodRecordsByPetId(petId);
//            return ResponseEntity.ok(responses);
//        }
//        catch (RuntimeException e) {
//            List<PetFoodRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.internalServerError().body(error);
//        }
//        catch (Exception e) {
//            List<PetFoodRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.badRequest().body(error);
//        }
//    }

    @GetMapping("/{petId}/food-records")
    public ResponseEntity<List<PetFoodRecordResponse>> findPetFoodRecordsByPetIdAndDate(
            @PathVariable Long petId,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try{
            List<PetFoodRecordResponse> responses = petService.getPetFoodRecordsByPetIdAndDate(petId, date);
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e) {
            List<PetFoodRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            List<PetFoodRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }
    }

//    @GetMapping("/{petId}/weight-records")
//    public ResponseEntity<List<PetWeightRecordResponse>> findPetWeightRecordsByPetId(@PathVariable Long petId) {
//        try{
//            List<PetWeightRecordResponse> responses = petService.getPetWeightRecordsByPetId(petId);
//            return ResponseEntity.ok(responses);
//        }
//        catch (RuntimeException e) {
//            List<PetWeightRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.internalServerError().body(error);
//        }
//        catch (Exception e) {
//            List<PetWeightRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.badRequest().body(error);
//        }
//    }
    @GetMapping("/{petId}/weight-records")
    public ResponseEntity<List<PetWeightRecordResponse>> findPetWeightRecordsByPetIdAndDate(
            @PathVariable Long petId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try{
            List<PetWeightRecordResponse> responses = petService.getPetWeightRecordsByPetIdAndDate(petId, date);
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e) {
            List<PetWeightRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            List<PetWeightRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }
    }

//    @GetMapping("/{petId}/image-records")
//    public ResponseEntity<List<PetImageRecordResponse>> findPetImageRecordsByPetId(@PathVariable Long petId) {
//        try{
//            List<PetImageRecordResponse> responses = petService.getPetImageRecordsByPetId(petId);
//            return ResponseEntity.ok(responses);
//        }
//        catch (RuntimeException e) {
//            List<PetImageRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.internalServerError().body(error);
//        }
//        catch (Exception e) {
//            List<PetImageRecordResponse> error = new  ArrayList<>();
//            return ResponseEntity.badRequest().body(error);
//        }
//    }
    @GetMapping("/{petId}/image-records")
    public ResponseEntity<List<PetImageRecordResponse>> findPetImageRecordsByPetIdAndDate(
            @PathVariable Long petId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try{
            List<PetImageRecordResponse> responses = petService.getPetImageRecordsByPetIdAndDate(petId, date);
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e) {
            List<PetImageRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            List<PetImageRecordResponse> error = new  ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ==================== ACTIVITY ENDPOINTS ====================

    @GetMapping("/{petId}/activities/daily")
    public ResponseEntity<DailyActivityResponse> getDailyActivities(
            @PathVariable Long petId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            DailyActivityResponse response = petService.getDailyActivities(petId, date);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DailyActivityResponse error = new DailyActivityResponse();
            error.setMessage("Error getting daily activities: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            DailyActivityResponse error = new DailyActivityResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{petId}/activities/range")
    public ResponseEntity<List<DailyActivityResponse>> getActivitiesInRange(
            @PathVariable Long petId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<DailyActivityResponse> response = petService.getActivitiesInRange(petId, startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

}

