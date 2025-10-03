package org.example.petcarebe.dto.request.invoice;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPrescriptionToInvoiceRequest {
    private Long prescriptionId;
    private Integer quantity;
    private Double taxPercent;
    private String notes;
}
