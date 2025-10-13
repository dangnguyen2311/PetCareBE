package org.example.petcarebe.model.dto;

import lombok.*;
import org.example.petcarebe.model.AbstractInvoiceItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionItemDTO {
    private Long id;
    private String dosage;
    private String duration;
    private String instruction;
    private Double price;
    private Integer quantity;
    private Double taxPercent;
    private Double promotionAmount;

    public Double getTotalPrice() {
        return getQuantity() * getPrice();
    }
    public Double getTotalTaxAmount() {
        return getTotalPrice() * getTaxPercent();
    }
    public Double getTaxAmount() {
        return getTotalPrice() * (getTaxPercent() / 100.0);
    }
    public Double getPromotionAmount() {
        return promotionAmount == null ? 0.0 : promotionAmount;
    }
}
