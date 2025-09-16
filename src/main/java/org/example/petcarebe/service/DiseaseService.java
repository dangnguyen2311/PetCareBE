package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.disease.CreateDiseaseRequest;
import org.example.petcarebe.dto.request.disease.UpdateDiseaseRequest;
import org.example.petcarebe.dto.response.disease.DiseaseResponse;
import org.example.petcarebe.dto.response.disease.DiseaseStatisticsResponse;
import org.example.petcarebe.model.Disease;
import org.example.petcarebe.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    /**
     * Create a new disease
     */
    @Transactional
    public DiseaseResponse createDisease(CreateDiseaseRequest request) {
        // Check if disease name already exists
        if (diseaseRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new RuntimeException("Disease with this name already exists");
        }

        Disease disease = new Disease();
        disease.setName(request.getName());
        disease.setDescription(request.getDescription());

        Disease savedDisease = diseaseRepository.save(disease);
        return convertToResponse(savedDisease, "Disease created successfully");
    }

    /**
     * Get all diseases
     */
    public List<DiseaseResponse> getAllDiseases() {
        return diseaseRepository.findAllByOrderByNameAsc().stream()
                .map(disease -> convertToResponse(disease, null))
                .collect(Collectors.toList());
    }

    /**
     * Get disease by ID
     */
    public DiseaseResponse getDiseaseById(Long id) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));
        return convertToResponse(disease, null);
    }

    /**
     * Update disease information
     */
    @Transactional
    public DiseaseResponse updateDisease(Long id, UpdateDiseaseRequest request) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));

        // Check if new name already exists (excluding current disease)
        if (request.getName() != null && !request.getName().equalsIgnoreCase(disease.getName())) {
            Optional<Disease> existingDisease = diseaseRepository.findByNameIgnoreCase(request.getName());
            if (existingDisease.isPresent() && !existingDisease.get().getId().equals(id)) {
                throw new RuntimeException("Disease with this name already exists");
            }
        }

        // Update fields if provided
        if (request.getName() != null) {
            disease.setName(request.getName());
        }
        if (request.getDescription() != null) {
            disease.setDescription(request.getDescription());
        }

        Disease updatedDisease = diseaseRepository.save(disease);
        return convertToResponse(updatedDisease, "Disease updated successfully");
    }

    /**
     * Delete disease
     */
    @Transactional
    public DiseaseResponse deleteDisease(Long id) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));

        // Check if disease is being used in any diagnosis
        // TODO: Add check for existing diagnoses

        diseaseRepository.delete(disease);
        return convertToResponse(disease, "Disease deleted successfully");
    }

    /**
     * Search diseases by keyword
     */
    public List<DiseaseResponse> searchDiseases(String keyword) {
        return diseaseRepository.searchByKeyword(keyword).stream()
                .map(disease -> convertToResponse(disease, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diseases by name containing
     */
    public List<DiseaseResponse> getDiseasesByName(String name) {
        return diseaseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(disease -> convertToResponse(disease, null))
                .collect(Collectors.toList());
    }

    /**
     * Get disease statistics
     */
    public DiseaseStatisticsResponse getDiseaseStatistics(Long id) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + id));

        // TODO: Implement actual statistics calculation from diagnosis data
        return DiseaseStatisticsResponse.builder()
                .diseaseId(disease.getId())
                .diseaseName(disease.getName())
                .description(disease.getDescription())
                .totalDiagnoses(0)
                .diagnosesThisMonth(0)
                .diagnosesThisWeek(0)
                .diagnosesToday(0)
                .totalPatientsAffected(0)
                .uniquePatientsThisMonth(0)
                .firstDiagnosedDate("N/A")
                .lastDiagnosedDate("N/A")
                .mostCommonAgeGroup("N/A")
                .averageDiagnosesPerMonth(0.0)
                .seasonalTrend("N/A")
                .totalTreatments(0)
                .successfulTreatments(0)
                .treatmentSuccessRate(0.0)
                .build();
    }

    /**
     * Get total count of diseases
     */
    public Long getTotalDiseaseCount() {
        return diseaseRepository.countTotalDiseases();
    }

    /**
     * Convert Disease entity to DiseaseResponse
     */
    private DiseaseResponse convertToResponse(Disease disease, String message) {
        DiseaseResponse response = new DiseaseResponse();
        response.setId(disease.getId());
        response.setName(disease.getName());
        response.setDescription(disease.getDescription());
        response.setMessage(message);
        return response;
    }
}

