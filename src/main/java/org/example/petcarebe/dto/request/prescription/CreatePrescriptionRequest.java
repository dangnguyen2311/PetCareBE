package org.example.petcarebe.dto.request.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePrescriptionRequest {

    // Optional - can be null when creating prescription before invoice
    private Long invoiceId;
    private String notes;
}
