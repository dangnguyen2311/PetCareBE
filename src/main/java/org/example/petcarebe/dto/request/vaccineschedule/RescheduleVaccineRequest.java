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
public class RescheduleVaccineRequest {
    
    @NotNull(message = "New scheduled date is required")
    private LocalDate newScheduledDate;
    
    private String reason;
    private String notes;
}
