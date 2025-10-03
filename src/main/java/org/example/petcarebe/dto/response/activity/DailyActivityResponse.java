package org.example.petcarebe.dto.response.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyActivityResponse {
    
    private LocalDate date;
    private Long petId;
    private String petName;
    private List<ActivityItem> activities;
    private Integer totalActivities;
    private String message;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityItem {
        private Long id;
        private String type; // FOOD, WEIGHT, IMAGE, APPOINTMENT, VISIT, VACCINE, DIAGNOSIS, TEST_RESULT
        private String title;
        private String description;
        private LocalDateTime timestamp;
        private LocalDate activityDate;
        private String status;
        private Object details; // Specific details based on activity type
        
        // Common fields
        private String notes;
        private String category;
        private String priority; // HIGH, MEDIUM, LOW
        
        // For medical activities
        private String doctorName;
        private String serviceName;
        
        // For records
        private String recordType;
        private String value;
        private String unit;
    }
}
