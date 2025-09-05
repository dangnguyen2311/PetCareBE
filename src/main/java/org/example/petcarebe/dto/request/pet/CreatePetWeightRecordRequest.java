package org.example.petcarebe.dto.request.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetWeightRecordRequest {
    private LocalDate recordDate;
    private Float weight;
    private String notes;
}
