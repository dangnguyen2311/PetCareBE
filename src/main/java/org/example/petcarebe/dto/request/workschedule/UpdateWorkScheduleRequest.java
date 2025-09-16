package org.example.petcarebe.dto.request.workschedule;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkScheduleRequest {

    private LocalDate workDate;
    
    private LocalTime startTime;
    
    private LocalTime endTime;
    
    private String notes;
}
