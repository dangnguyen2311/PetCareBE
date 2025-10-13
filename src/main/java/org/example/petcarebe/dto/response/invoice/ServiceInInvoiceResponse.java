package org.example.petcarebe.dto.response.invoice;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInInvoiceResponse {
    private Long id;
    private Double price;
    private Integer quantity;
    private Double taxAmount;
    private Double promotionAmount;
    private String notes;
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceCategory;
    private String message;
}
