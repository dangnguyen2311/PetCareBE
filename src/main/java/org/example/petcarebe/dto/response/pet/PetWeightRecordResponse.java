package org.example.petcarebe.dto.response.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWeightRecordResponse {
    private Long id;
    private LocalDate recordDate;
    private Float weight;
    private String notes;
    private String message;

}
