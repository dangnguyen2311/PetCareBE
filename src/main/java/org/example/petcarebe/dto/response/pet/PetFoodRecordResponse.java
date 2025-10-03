package org.example.petcarebe.dto.response.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetFoodRecordResponse {
    private Long id;
    private LocalDate recordDate;
    private String foodType;
    private String amountFood;
    private String notes;
    private String message;
}
