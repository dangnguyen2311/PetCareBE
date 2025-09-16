package org.example.petcarebe.dto.request.workschedule;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkScheduleRequest {
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Work date is required")
    private LocalDate workDate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    private String notes;
}
