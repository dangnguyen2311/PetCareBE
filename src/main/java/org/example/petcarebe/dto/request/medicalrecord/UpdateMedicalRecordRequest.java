package org.example.petcarebe.dto.request.medicalrecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRecordRequest {
    private String summary;
}
