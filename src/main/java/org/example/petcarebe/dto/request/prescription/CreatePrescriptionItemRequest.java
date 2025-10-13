package org.example.petcarebe.dto.request.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePrescriptionItemRequest {
    
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;
    
    @NotBlank(message = "Dosage is required")
    private String dosage;
    
    @NotBlank(message = "Duration is required")
    private String duration;
    private Integer quantity;
    
    @NotBlank(message = "Instruction is required")
    private String instruction;
    
    @NotNull(message = "Tax percent is required")
    @PositiveOrZero(message = "Tax percent must be zero or positive")
    private Double taxPercent;

}
