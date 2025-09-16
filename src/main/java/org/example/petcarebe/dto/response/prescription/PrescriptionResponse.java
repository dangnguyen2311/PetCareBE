package org.example.petcarebe.dto.response.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionResponse {
    private Long id;
    private LocalDate createdDate;
    private String note;
    private Long invoiceId;
}
