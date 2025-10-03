package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductToInvoiceResponse {
    private Long invoiceId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private String productImgUrl;
    private String productBrand;
    private String productUnit;
    private String productSupplier;
    private Integer quantity;
    private Double taxPercent;
    private Double promotionAmount;
    private String notes;
    private String message;
}
