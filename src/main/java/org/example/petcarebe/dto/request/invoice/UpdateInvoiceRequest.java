package org.example.petcarebe.dto.request.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInvoiceRequest {
    private String customerName;
    private String customerPhone;
    private String notes;
    private Long staffId;
    private Long customerId;
}
