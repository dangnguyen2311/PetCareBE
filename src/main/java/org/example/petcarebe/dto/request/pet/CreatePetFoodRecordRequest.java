package org.example.petcarebe.dto.request.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePetFoodRecordRequest {
    private LocalDate recordDate;
    private String foodType;
    private String amountFood;
    private String notes;
}
