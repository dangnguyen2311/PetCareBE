package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddServicePackageToInvoiceResponse {
    private Long invoiceId;
    private Long servicePackageId;
    private String servicePackageName;
    private String servicePackageDescription;
    private String servicePackageImgUrl;
    private Double servicePackagePrice;
    private Integer quantity;
    private Double taxPercent;
    private Double promotionAmount;
    private String notes;
    private String message;
}
