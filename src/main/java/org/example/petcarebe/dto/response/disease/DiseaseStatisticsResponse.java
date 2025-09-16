package org.example.petcarebe.dto.response.disease;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseStatisticsResponse {
    // Disease basic info
    private Long diseaseId;
    private String diseaseName;
    private String description;
    
    // Diagnosis statistics
    private Integer totalDiagnoses;
    private Integer diagnosesThisMonth;
    private Integer diagnosesThisWeek;
    private Integer diagnosesToday;
    
    // Patient statistics
    private Integer totalPatientsAffected;
    private Integer uniquePatientsThisMonth;
    
    // Time-based statistics
    private String firstDiagnosedDate;
    private String lastDiagnosedDate;
    private String mostCommonAgeGroup;
    
    // Frequency statistics
    private Double averageDiagnosesPerMonth;
    private String seasonalTrend; // Which season this disease is most common
    
    // Treatment statistics
    private Integer totalTreatments;
    private Integer successfulTreatments;
    private Double treatmentSuccessRate;
}
