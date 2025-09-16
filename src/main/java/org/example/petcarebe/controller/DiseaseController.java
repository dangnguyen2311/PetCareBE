package org.example.petcarebe.controller;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.disease.CreateDiseaseRequest;
import org.example.petcarebe.dto.request.disease.UpdateDiseaseRequest;
import org.example.petcarebe.dto.response.disease.DiseaseResponse;
import org.example.petcarebe.dto.response.disease.DiseaseStatisticsResponse;
import org.example.petcarebe.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    /**
     * Create a new disease
     * Only ADMIN and DOCTOR can create diseases
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiseaseResponse> createDisease(@Valid @RequestBody CreateDiseaseRequest request) {
        try {
            DiseaseResponse response = diseaseService.createDisease(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            DiseaseResponse errorResponse = new DiseaseResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all diseases
     * Accessible by all authenticated users
     */
    @GetMapping
    public ResponseEntity<List<DiseaseResponse>> getAllDiseases() {
        List<DiseaseResponse> diseases = diseaseService.getAllDiseases();
        return ResponseEntity.ok(diseases);
    }

    /**
     * Get disease by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiseaseResponse> getDiseaseById(@PathVariable Long id) {
        try {
            DiseaseResponse disease = diseaseService.getDiseaseById(id);
            return ResponseEntity.ok(disease);
        } catch (RuntimeException e) {
            DiseaseResponse errorResponse = new DiseaseResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Update disease information
     * Only ADMIN and DOCTOR can update diseases
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiseaseResponse> updateDisease(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateDiseaseRequest request) {
        try {
            DiseaseResponse response = diseaseService.updateDisease(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DiseaseResponse errorResponse = new DiseaseResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete disease
     * Only ADMIN can delete diseases
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiseaseResponse> deleteDisease(@PathVariable Long id) {
        try {
            DiseaseResponse response = diseaseService.deleteDisease(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DiseaseResponse errorResponse = new DiseaseResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Search diseases by keyword
     * Accessible by all authenticated users
     */
    @GetMapping("/search")
    public ResponseEntity<List<DiseaseResponse>> searchDiseases(@RequestParam String keyword) {
        List<DiseaseResponse> diseases = diseaseService.searchDiseases(keyword);
        return ResponseEntity.ok(diseases);
    }

    /**
     * Get diseases by name
     * Accessible by all authenticated users
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<DiseaseResponse>> getDiseasesByName(@PathVariable String name) {
        List<DiseaseResponse> diseases = diseaseService.getDiseasesByName(name);
        return ResponseEntity.ok(diseases);
    }

    /**
     * Get disease statistics
     * Accessible by ADMIN and DOCTOR
     */
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiseaseStatisticsResponse> getDiseaseStatistics(@PathVariable Long id) {
        try {
            DiseaseStatisticsResponse stats = diseaseService.getDiseaseStatistics(id);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get total disease count
     * Accessible by ADMIN and DOCTOR
     */
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Long> getTotalDiseaseCount() {
        Long count = diseaseService.getTotalDiseaseCount();
        return ResponseEntity.ok(count);
    }
}

