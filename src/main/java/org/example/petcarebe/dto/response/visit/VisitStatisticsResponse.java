package org.example.petcarebe.dto.response.visit;

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
public class VisitStatisticsResponse {
    
    // Date range for statistics
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Overall statistics
    private Long totalVisits;
    private Long totalPets;
    private Long totalCustomers;
    private Long totalDoctors;
    
    // Daily statistics (date -> count)
    private Map<LocalDate, Long> dailyVisitCounts;
    
    // Monthly statistics (year-month -> count)
    private Map<String, Long> monthlyVisitCounts;
    
    // Doctor performance
    private List<DoctorPerformanceData> doctorPerformance;
    
    // Most visited pets
    private List<PetVisitData> mostVisitedPets;
    
    // Clinic room utilization
    private List<ClinicRoomUtilizationData> clinicRoomUtilization;
    
    // Response message
    private String message;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DoctorPerformanceData {
        private Long doctorId;
        private String doctorName;
        private String specialization;
        private Long visitCount;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PetVisitData {
        private Long petId;
        private String petName;
        private String petType;
        private Long visitCount;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClinicRoomUtilizationData {
        private Long clinicRoomId;
        private String clinicRoomName;
        private Long visitCount;
    }
}
