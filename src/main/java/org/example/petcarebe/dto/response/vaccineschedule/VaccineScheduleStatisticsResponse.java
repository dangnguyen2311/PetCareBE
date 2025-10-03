package org.example.petcarebe.dto.response.vaccineschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineScheduleStatisticsResponse {
    
    // Date range for statistics
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Overall statistics
    private Long totalSchedules;
    private Long scheduledCount;
    private Long completedCount;
    private Long cancelledCount;
    private Long overdueCount;
    private Long upcomingCount;
    
    // Status distribution
    private Map<String, Long> statusDistribution;
    
    // Daily statistics (date -> count)
    private Map<LocalDate, Long> dailyScheduleCounts;
    
    // Monthly statistics (year-month -> count)
    private Map<String, Long> monthlyScheduleCounts;
    
    // Most scheduled vaccines
    private List<VaccineStatData> mostScheduledVaccines;
    
    // Pets with most schedules
    private List<PetScheduleData> petsWithMostSchedules;
    
    // Response message
    private String message;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VaccineStatData {
        private Long vaccineId;
        private String vaccineName;
        private Long scheduleCount;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PetScheduleData {
        private Long petId;
        private String petName;
        private String petType;
        private Long scheduleCount;
    }
}
