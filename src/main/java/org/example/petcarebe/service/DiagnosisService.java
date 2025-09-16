package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.diagnosis.CreateDiagnosisRequest;
import org.example.petcarebe.dto.request.diagnosis.UpdateDiagnosisRequest;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisResponse;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisStatisticsResponse;
import org.example.petcarebe.model.Diagnosis;
import org.example.petcarebe.model.Disease;
import org.example.petcarebe.model.Visit;
import org.example.petcarebe.repository.DiagnosisRepository;
import org.example.petcarebe.repository.DiseaseRepository;
import org.example.petcarebe.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    /**
     * Create a new diagnosis
     */
    @Transactional
    public DiagnosisResponse createDiagnosis(CreateDiagnosisRequest request) {
        // Validate visit exists
        Visit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + request.getVisitId()));

        // Validate disease exists
        Disease disease = diseaseRepository.findById(request.getDiseaseId())
                .orElseThrow(() -> new RuntimeException("Disease not found with id: " + request.getDiseaseId()));

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setVisit(visit);
        diagnosis.setDisease(disease);
        diagnosis.setDescription(request.getDescription());
        diagnosis.setCreatedDate(LocalDate.now());

        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        return convertToResponse(savedDiagnosis, "Diagnosis created successfully");
    }

    /**
     * Get all diagnoses
     */
    public List<DiagnosisResponse> getAllDiagnoses() {
        return diagnosisRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnosis by ID
     */
    public DiagnosisResponse getDiagnosisById(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id));
        return convertToResponse(diagnosis, null);
    }

    /**
     * Update diagnosis
     */
    @Transactional
    public DiagnosisResponse updateDiagnosis(Long id, UpdateDiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id));

        // Update disease if provided
        if (request.getDiseaseId() != null) {
            Disease disease = diseaseRepository.findById(request.getDiseaseId())
                    .orElseThrow(() -> new RuntimeException("Disease not found with id: " + request.getDiseaseId()));
            diagnosis.setDisease(disease);
        }

        // Update description if provided
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            diagnosis.setDescription(request.getDescription());
        }

        Diagnosis updatedDiagnosis = diagnosisRepository.save(diagnosis);
        return convertToResponse(updatedDiagnosis, "Diagnosis updated successfully");
    }

    /**
     * Delete diagnosis
     */
    @Transactional
    public DiagnosisResponse deleteDiagnosis(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id));

        diagnosisRepository.delete(diagnosis);
        return convertToResponse(diagnosis, "Diagnosis deleted successfully");
    }

    /**
     * Get diagnoses by visit ID
     */
    public List<DiagnosisResponse> getDiagnosesByVisitId(Long visitId) {
        return diagnosisRepository.findByVisitId(visitId).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses by disease ID
     */
    public List<DiagnosisResponse> getDiagnosesByDiseaseId(Long diseaseId) {
        return diagnosisRepository.findByDiseaseId(diseaseId).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses by pet ID
     */
    public List<DiagnosisResponse> getDiagnosesByPetId(Long petId) {
        return diagnosisRepository.findByPetId(petId).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses by customer ID
     */
    public List<DiagnosisResponse> getDiagnosesByCustomerId(Long customerId) {
        return diagnosisRepository.findByCustomerId(customerId).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses by doctor ID
     */
    public List<DiagnosisResponse> getDiagnosesByDoctorId(Long doctorId) {
        return diagnosisRepository.findByDoctorId(doctorId).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses by date range
     */
    public List<DiagnosisResponse> getDiagnosesByDateRange(LocalDate startDate, LocalDate endDate) {
        return diagnosisRepository.findByCreatedDateBetween(startDate, endDate).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Search diagnoses by description
     */
    public List<DiagnosisResponse> searchDiagnoses(String keyword) {
        return diagnosisRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(diagnosis -> convertToResponse(diagnosis, null))
                .collect(Collectors.toList());
    }

    /**
     * Get diagnosis statistics
     */
    public DiagnosisStatisticsResponse getDiagnosisStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);

        // Basic counts
        Long totalDiagnoses = diagnosisRepository.count();
        Long diagnosesToday = diagnosisRepository.countByCreatedDate(today);
        Long diagnosesThisWeek = diagnosisRepository.countByCreatedDateBetween(weekStart, today);
        Long diagnosesThisMonth = diagnosisRepository.countByCreatedDateBetween(monthStart, today);
        Long diagnosesThisYear = diagnosisRepository.countByCreatedDateBetween(yearStart, today);

        // Disease statistics
        List<Object[]> diseaseStats = diagnosisRepository.findMostCommonDiseases();
        List<DiagnosisStatisticsResponse.DiseaseStatistic> topDiseases = diseaseStats.stream()
                .limit(5)
                .map(row -> DiagnosisStatisticsResponse.DiseaseStatistic.builder()
                        .diseaseId((Long) row[0])
                        .diseaseName((String) row[1])
                        .count((Long) row[2])
                        .percentage(totalDiagnoses > 0 ? ((Long) row[2] * 100.0) / totalDiagnoses : 0.0)
                        .build())
                .collect(Collectors.toList());

        // Doctor statistics
        List<Object[]> doctorStats = diagnosisRepository.findMostActiveDoctors();
        List<DiagnosisStatisticsResponse.DoctorStatistic> topDoctors = doctorStats.stream()
                .limit(5)
                .map(row -> DiagnosisStatisticsResponse.DoctorStatistic.builder()
                        .doctorId((Long) row[0])
                        .doctorName((String) row[1])
                        .specialization((String) row[2])
                        .diagnosesCount((Long) row[3])
                        .percentage(totalDiagnoses > 0 ? ((Long) row[3] * 100.0) / totalDiagnoses : 0.0)
                        .build())
                .collect(Collectors.toList());

        return DiagnosisStatisticsResponse.builder()
                .totalDiagnoses(totalDiagnoses)
                .diagnosesToday(diagnosesToday)
                .diagnosesThisWeek(diagnosesThisWeek)
                .diagnosesThisMonth(diagnosesThisMonth)
                .diagnosesThisYear(diagnosesThisYear)
                .totalUniqueDiseases((long) diseaseStats.size())
                .mostCommonDisease(topDiseases.isEmpty() ? "N/A" : topDiseases.get(0).getDiseaseName())
                .mostCommonDiseaseCount(topDiseases.isEmpty() ? 0L : topDiseases.get(0).getCount())
                .topDiseases(topDiseases)
                .totalDoctorsInvolved((long) doctorStats.size())
                .mostActiveDoctorName(topDoctors.isEmpty() ? "N/A" : topDoctors.get(0).getDoctorName())
                .mostActiveDoctorDiagnoses(topDoctors.isEmpty() ? 0L : topDoctors.get(0).getDiagnosesCount())
                .topDoctors(topDoctors)
                .averageDiagnosesPerDay(totalDiagnoses > 0 ? totalDiagnoses / (double) ChronoUnit.DAYS.between(yearStart, today.plusDays(1)) : 0.0)
                .averageDiagnosesPerWeek(diagnosesThisYear / 52.0)
                .averageDiagnosesPerMonth(diagnosesThisYear / 12.0)
                .monthlyTrend("stable") // TODO: Calculate actual trend
                .monthlyGrowthRate(0.0) // TODO: Calculate actual growth rate
                .build();
    }

    /**
     * Convert Diagnosis entity to DiagnosisResponse
     */
    private DiagnosisResponse convertToResponse(Diagnosis diagnosis, String message) {
        DiagnosisResponse.DiagnosisResponseBuilder builder = DiagnosisResponse.builder()
                .id(diagnosis.getId())
                .description(diagnosis.getDescription())
                .createdDate(diagnosis.getCreatedDate())
                .message(message);

        // Visit information
        if (diagnosis.getVisit() != null) {
            Visit visit = diagnosis.getVisit();
            builder.visitId(visit.getId())
                   .visitDate(visit.getVisitDate())
                   .visitNotes(visit.getNotes());

            // Pet information
            if (visit.getPet() != null) {
                builder.petId(visit.getPet().getId())
                       .petName(visit.getPet().getName())
                       .petType(visit.getPet().getAnimalType() != null ?
                               visit.getPet().getAnimalType().getName() : "Unknown");

                // Customer information
                if (visit.getPet().getCustomer() != null) {
                    builder.customerId(visit.getPet().getCustomer().getId())
                           .customerName(visit.getPet().getCustomer().getFullname())
                           .customerEmail(visit.getPet().getCustomer().getEmail());
                }
            }

            // Doctor information
            if (visit.getWorkSchedule().getDoctor() != null) {
                builder.doctorId(visit.getWorkSchedule().getDoctor().getId())
                       .doctorName(visit.getWorkSchedule().getDoctor().getFullname())
                       .doctorSpecialization(visit.getWorkSchedule().getDoctor().getSpecialization());
            }
        }

        // Disease information
        if (diagnosis.getDisease() != null) {
            builder.diseaseId(diagnosis.getDisease().getId())
                   .diseaseName(diagnosis.getDisease().getName())
                   .diseaseDescription(diagnosis.getDisease().getDescription());
        }

        return builder.build();
    }
}

