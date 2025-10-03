package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.vaccine.CreateVaccineRequest;
import org.example.petcarebe.dto.request.vaccine.UpdateVaccineRequest;
import org.example.petcarebe.dto.response.vaccinationrecord.VaccinationRecordResponse;
import org.example.petcarebe.dto.response.vaccine.VaccineResponse;
import org.example.petcarebe.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1/vaccines")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<VaccineResponse> createVaccine(@RequestBody CreateVaccineRequest request) {
        try {
            VaccineResponse createdVaccine = vaccineService.createVaccine(request);
            return ResponseEntity.ok(createdVaccine);
        } catch (RuntimeException e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorVaccine);
        }
        catch (Exception e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVaccine);
        }
    }

    @GetMapping
    public ResponseEntity<List<VaccineResponse>> getAllVaccines() {
        try {
            List<VaccineResponse> vaccines = vaccineService.getAllVaccines();
            return ResponseEntity.ok(vaccines);
        } catch (RuntimeException e) {
            List<VaccineResponse> errorVaccines = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorVaccines);
        }
        catch (Exception e) {
            List<VaccineResponse> errorVaccines = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVaccines);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccineResponse> getVaccineById(@PathVariable Long id) {
        try {
            VaccineResponse vaccine = vaccineService.getVaccineById(id);
            return ResponseEntity.ok(vaccine);
        } catch (RuntimeException e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorVaccine);
        }catch (Exception e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVaccine);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaccineResponse> updateVaccine(@PathVariable Long id, @RequestBody UpdateVaccineRequest request) {
        try {
            VaccineResponse updatedVaccine = vaccineService.updateVaccine(id, request);
            return ResponseEntity.ok(updatedVaccine);
        } catch (RuntimeException e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorVaccine);
        }catch (Exception e) {
            VaccineResponse errorVaccine = new VaccineResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVaccine);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<VaccinationRecordResponse>> getVaccineByPetId(@PathVariable Long petId) {
        try{
            List<VaccinationRecordResponse> responses = vaccineService.getAllVaccinesByPetId(petId);
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e){
            List<VaccinationRecordResponse> errorVaccines = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorVaccines);
        }
        catch (Exception e) {
            List<VaccinationRecordResponse> errorVaccines = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVaccines);
        }
    }
}

