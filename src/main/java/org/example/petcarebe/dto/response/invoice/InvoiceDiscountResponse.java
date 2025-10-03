package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDiscountResponse {
    private Long discountId;
    private Long invoiceId;
    private Double appliedAmount;
    private String message;
}
