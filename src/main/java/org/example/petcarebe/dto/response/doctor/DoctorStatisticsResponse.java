package org.example.petcarebe.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorStatisticsResponse {
    // Doctor basic info
    private Long doctorId;
    private String doctorName;
    private String specialization;
    
    // Visit statistics
    private Integer totalVisits;
    private Integer visitsThisMonth;
    private Integer visitsThisWeek;
    private Integer visitsToday;
    
    // Patient statistics
    private Integer totalPatientsServed;
    private Integer uniquePatientsThisMonth;
    
    // Diagnosis statistics
    private Integer totalDiagnoses;
    private Integer diagnosesThisMonth;
    
    // Vaccination statistics
    private Integer totalVaccinations;
    private Integer vaccinationsThisMonth;
    
    // Work schedule statistics
    private Integer scheduledDaysThisMonth;
    private Integer workingDaysThisMonth;
    
    // Performance metrics
    private Double averageVisitsPerDay;
    private Double averagePatientsPerDay;
    
    // Time-based statistics
    private String workingSince; // How long they've been working
    private String lastWorkDate;
    private String nextScheduledDate;
}
