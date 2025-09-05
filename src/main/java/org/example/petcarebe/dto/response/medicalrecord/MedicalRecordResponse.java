package org.example.petcarebe.dto.response.medicalrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String summary;
    private String message;
    private Long petId;
}
