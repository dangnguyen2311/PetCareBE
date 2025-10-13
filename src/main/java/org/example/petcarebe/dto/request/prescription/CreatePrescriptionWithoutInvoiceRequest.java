package org.example.petcarebe.dto.request.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePrescriptionWithoutInvoiceRequest {
    private Long doctorId;
    private String notes;
}
