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
public class PetImageRecordResponse {
    private Long id;
    private LocalDate recordDate;
    private String imgUrl;
    private String notes;
    private String message;
}
