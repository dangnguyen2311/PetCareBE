package org.example.petcarebe.dto.request.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddServiceToInvoiceRequest {
    private Long serviceId;
    private Integer quantity;
    private Double taxPercent;
    private String notes;
}
