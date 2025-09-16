package org.example.petcarebe.dto.response.workschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkScheduleStatisticsResponse {
    // Doctor basic info
    private Long doctorId;
    private String doctorName;
    private String specialization;
    
    // Schedule statistics
    private Integer totalWorkingDays;
    private Integer totalWorkingHours;
    private Double averageHoursPerDay;
    
    // Schedule breakdown
    private List<DayScheduleInfo> scheduleList;

    // Visit statistics based on schedule
    private Integer totalVisitsScheduled;
    private Integer totalVisitsCompleted;
    private Double scheduleUtilizationRate;

    // Time-based statistics
    private String earliestStartTime;
    private String latestEndTime;
    private String mostBusyDate;
    private String leastBusyDate;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DayScheduleInfo {
        private String workDate;
        private String startTime;
        private String endTime;
        private Integer workingHours;
        private Integer scheduledVisits;
        private String notes;
    }
}
