package org.example.petcarebe.dto.request.prescription;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePrescriptionInvoiceRequest {
    
    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;
}
