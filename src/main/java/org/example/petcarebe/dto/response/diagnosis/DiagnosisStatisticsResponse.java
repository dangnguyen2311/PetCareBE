package org.example.petcarebe.dto.response.diagnosis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiagnosisStatisticsResponse {
    
    // Basic statistics
    private Long totalDiagnoses;
    private Long diagnosesToday;
    private Long diagnosesThisWeek;
    private Long diagnosesThisMonth;
    private Long diagnosesThisYear;
    
    // Disease statistics
    private Long totalUniqueDiseases;
    private String mostCommonDisease;
    private Long mostCommonDiseaseCount;
    private List<DiseaseStatistic> topDiseases;
    
    // Doctor statistics
    private Long totalDoctorsInvolved;
    private String mostActiveDoctorName;
    private Long mostActiveDoctorDiagnoses;
    private List<DoctorStatistic> topDoctors;
    
    // Pet statistics
    private Long totalPetsAffected;
    private String mostAffectedPetType;
    private Long mostAffectedPetTypeCount;
    private List<PetTypeStatistic> petTypeStats;
    
    // Time-based statistics
    private Double averageDiagnosesPerDay;
    private Double averageDiagnosesPerWeek;
    private Double averageDiagnosesPerMonth;
    private String peakDiagnosisMonth;
    private String peakDiagnosisDay;
    
    // Trend analysis
    private String monthlyTrend; // "increasing", "decreasing", "stable"
    private Double monthlyGrowthRate;
    private String seasonalPattern;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiseaseStatistic {
        private Long diseaseId;
        private String diseaseName;
        private Long count;
        private Double percentage;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DoctorStatistic {
        private Long doctorId;
        private String doctorName;
        private String specialization;
        private Long diagnosesCount;
        private Double percentage;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PetTypeStatistic {
        private String petType;
        private Long count;
        private Double percentage;
    }
}
