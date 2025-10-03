package org.example.petcarebe.dto.request.vaccineschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVaccineScheduleRequest {
    
    private LocalDate scheduledDate;
    private String status;
    private String notes;
}
