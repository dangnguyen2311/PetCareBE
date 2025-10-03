package org.example.petcarebe.controller.admin;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.diagnosis.CreateDiagnosisRequest;
import org.example.petcarebe.dto.request.diagnosis.UpdateDiagnosisRequest;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisResponse;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisStatisticsResponse;
import org.example.petcarebe.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/diagnoses")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    /**
     * Create a new diagnosis
     * Only ADMIN and DOCTOR can create diagnoses
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiagnosisResponse> createDiagnosis(@Valid @RequestBody CreateDiagnosisRequest request) {
        try {
            DiagnosisResponse response = diagnosisService.createDiagnosis(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Error creating diagnosis: " + e.getMessage());

            DiagnosisResponse errorResponse = new DiagnosisResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            DiagnosisResponse errorResponse = new DiagnosisResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all diagnoses
     * Accessible by all authenticated users
     */
    @GetMapping
    public ResponseEntity<List<DiagnosisResponse>> getAllDiagnoses() {
        List<DiagnosisResponse> diagnoses = diagnosisService.getAllDiagnoses();
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnosis by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisResponse> getDiagnosisById(@PathVariable Long id) {
        try {
            DiagnosisResponse response = diagnosisService.getDiagnosisById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting diagnosis: " + e.getMessage());

            DiagnosisResponse errorResponse = new DiagnosisResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Update diagnosis
     * Only ADMIN and DOCTOR can update diagnoses
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiagnosisResponse> updateDiagnosis(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateDiagnosisRequest request) {
        try {
            DiagnosisResponse response = diagnosisService.updateDiagnosis(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating diagnosis: " + e.getMessage());

            DiagnosisResponse errorResponse = new DiagnosisResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete diagnosis
     * Only ADMIN can delete diagnoses
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiagnosisResponse> deleteDiagnosis(@PathVariable Long id) {
        try {
            DiagnosisResponse response = diagnosisService.deleteDiagnosis(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error deleting diagnosis: " + e.getMessage());

            DiagnosisResponse errorResponse = new DiagnosisResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get diagnoses by visit ID
     */
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByVisitId(@PathVariable Long visitId) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByVisitId(visitId);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnoses by disease ID
     */
    @GetMapping("/disease/{diseaseId}")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByDiseaseId(@PathVariable Long diseaseId) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByDiseaseId(diseaseId);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnoses by pet ID
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByPetId(@PathVariable Long petId) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByPetId(petId);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnoses by customer ID
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByCustomerId(@PathVariable Long customerId) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByCustomerId(customerId);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnoses by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByDoctorId(@PathVariable Long doctorId) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByDoctorId(doctorId);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnoses by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<DiagnosisResponse>> getDiagnosesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByDateRange(startDate, endDate);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Search diagnoses by description keyword
     */
    @GetMapping("/search")
    public ResponseEntity<List<DiagnosisResponse>> searchDiagnoses(@RequestParam String keyword) {
        List<DiagnosisResponse> diagnoses = diagnosisService.searchDiagnoses(keyword);
        return ResponseEntity.ok(diagnoses);
    }

    /**
     * Get diagnosis statistics
     * Only ADMIN and DOCTOR can view statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DiagnosisStatisticsResponse> getDiagnosisStatistics() {
        DiagnosisStatisticsResponse statistics = diagnosisService.getDiagnosisStatistics();
        return ResponseEntity.ok(statistics);
    }
}

