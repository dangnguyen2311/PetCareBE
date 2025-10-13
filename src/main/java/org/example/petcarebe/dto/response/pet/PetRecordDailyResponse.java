package org.example.petcarebe.dto.response.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRecordDailyResponse {
    private Long petId;
    private List<PetFoodRecordResponse>  petFoodRecords;
    private List<PetWeightRecordResponse>  petWeightRecords;
    private List<PetImageRecordResponse>   petImageRecords;
    private String message;
}
