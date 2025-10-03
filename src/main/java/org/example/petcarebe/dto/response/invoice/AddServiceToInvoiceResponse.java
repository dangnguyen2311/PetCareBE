package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddServiceToInvoiceResponse {
    private Long invoiceId;
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceImgUrl;
    private String serviceCategory;
    private Double servicePrice;
    private Integer quantity;
    private Double taxPercent;
    private Double promotionAmount;
    private String notes;
    private String message;

}
