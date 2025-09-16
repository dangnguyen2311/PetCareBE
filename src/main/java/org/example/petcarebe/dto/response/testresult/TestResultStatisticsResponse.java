package org.example.petcarebe.dto.response.testresult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultStatisticsResponse {
    
    // Basic statistics
    private Long totalTestResults;
    private Long testResultsToday;
    private Long testResultsThisWeek;
    private Long testResultsThisMonth;
    private Long testResultsThisYear;
    
    // Test type statistics
    private Long totalUniqueTestTypes;
    private String mostCommonTestType;
    private Long mostCommonTestTypeCount;
    private List<TestTypeStatistic> topTestTypes;
    
    // Result pattern statistics
    private Long normalResults;
    private Long abnormalResults;
    private Double normalResultPercentage;
    private Double abnormalResultPercentage;
    private List<ResultPatternStatistic> resultPatterns;
    
    // Doctor statistics
    private Long totalDoctorsInvolved;
    private String mostActiveDoctorName;
    private Long mostActiveDoctorTests;
    private List<DoctorStatistic> topDoctors;
    
    // Pet statistics
    private Long totalPetsAffected;
    private String mostTestedPetType;
    private Long mostTestedPetTypeCount;
    private List<PetTypeStatistic> petTypeStats;
    
    // Time-based statistics
    private Double averageTestsPerDay;
    private Double averageTestsPerWeek;
    private Double averageTestsPerMonth;
    private String peakTestingMonth;
    private String peakTestingDay;
    
    // Trend analysis
    private String monthlyTrend; // "increasing", "decreasing", "stable"
    private Double monthlyGrowthRate;
    private String seasonalPattern;
    
    // Quality metrics
    private Double testCompletionRate;
    private Double averageTestsPerVisit;
    private String mostFrequentTestTime;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TestTypeStatistic {
        private String testType;
        private Long count;
        private Double percentage;
        private Long normalCount;
        private Long abnormalCount;
        private Double normalRate;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResultPatternStatistic {
        private String pattern;
        private Long count;
        private Double percentage;
        private String description;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DoctorStatistic {
        private Long doctorId;
        private String doctorName;
        private String specialization;
        private Long testsCount;
        private Double percentage;
        private String mostCommonTestType;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PetTypeStatistic {
        private String petType;
        private Long count;
        private Double percentage;
        private String mostCommonTestType;
        private Double averageTestsPerPet;
    }
}
