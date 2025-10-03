package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPrescriptionToInvoiceResponse {
    private Long invoiceId;
    private Long prescriptionId;
    private LocalDate prescriptionCreatedDate;
    private String prescriptionNotes;
    private String message;
}
