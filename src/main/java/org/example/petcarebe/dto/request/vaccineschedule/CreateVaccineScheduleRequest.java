package org.example.petcarebe.dto.request.vaccineschedule;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVaccineScheduleRequest {
    
    @NotNull(message = "Pet ID is required")
    private Long petId;
    
    @NotNull(message = "Vaccine ID is required")
    private Long vaccineId;
    
    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;
    
    private String status = "SCHEDULED"; // Default status
    
    private String notes;
}
