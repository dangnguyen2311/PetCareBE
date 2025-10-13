package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPromotionToInvoiceResponse {
    private Long invoiceId;
    private Long promotionId;
    private String promotionName;
    private String promotionType;
    private Double promotionValue;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
    private String promotionStatus;
    private String message;
}
