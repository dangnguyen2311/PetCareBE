package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.vaccine.CreateVaccineRequest;
import org.example.petcarebe.dto.request.vaccine.UpdateVaccineRequest;
import org.example.petcarebe.dto.response.vaccine.VaccineResponse;
import org.example.petcarebe.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/vaccines")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<VaccineResponse> createVaccine(@RequestBody CreateVaccineRequest request) {
        VaccineResponse createdVaccine = vaccineService.createVaccine(request);
        return ResponseEntity.ok(createdVaccine);
    }

    @GetMapping
    public ResponseEntity<List<VaccineResponse>> getAllVaccines() {
        List<VaccineResponse> vaccines = vaccineService.getAllVaccines();
        return ResponseEntity.ok(vaccines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccineResponse> getVaccineById(@PathVariable Long id) {
        VaccineResponse vaccine = vaccineService.getVaccineById(id);
        return ResponseEntity.ok(vaccine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaccineResponse> updateVaccine(@PathVariable Long id, @RequestBody UpdateVaccineRequest request) {
        VaccineResponse updatedVaccine = vaccineService.updateVaccine(id, request);
        return ResponseEntity.ok(updatedVaccine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }
}

