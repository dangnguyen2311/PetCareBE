package org.example.petcarebe.dto.response.workschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkScheduleResponse {
    private Long id;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
    
    // Doctor information
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private String doctorPhone;
    private String doctorEmail;
    
    // Additional information
    private String message;
    private Integer totalHours; // Calculated field for total working hours
}
