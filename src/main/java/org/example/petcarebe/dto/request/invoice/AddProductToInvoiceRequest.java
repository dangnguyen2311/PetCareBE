package org.example.petcarebe.dto.request.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductToInvoiceRequest {
    private Long productId;
    private Integer quantity;
    private Double taxPercent;
    private String notes;
}
