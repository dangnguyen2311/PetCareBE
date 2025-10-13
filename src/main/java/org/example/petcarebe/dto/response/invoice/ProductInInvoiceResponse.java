package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInInvoiceResponse {
    private Long id;
    private Double price;
    private Integer quantity;
    private Double taxAmount;
    private Double promotionAmount;
    private String notes;
    private Long productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private String message;
}
