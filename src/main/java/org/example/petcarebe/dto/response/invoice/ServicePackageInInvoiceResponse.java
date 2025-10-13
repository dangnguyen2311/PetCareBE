package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePackageInInvoiceResponse {
    private Long id;
    private Double price;
    private Integer quantity;
    private Double taxAmount;
    private Double promotionAmount;
    private String notes;
    private Long servicePackageId;
    private String servicePackageName;
    private String servicePackageDescription;
    private String message;
}
