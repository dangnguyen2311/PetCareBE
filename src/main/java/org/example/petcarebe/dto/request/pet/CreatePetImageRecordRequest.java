package org.example.petcarebe.dto.request.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePetImageRecordRequest {
    private LocalDate recordDate;
    private String imgUrl;
    private String notes;
}
