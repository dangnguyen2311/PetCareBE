package org.example.petcarebe.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;

@MappedSuperclass
@Setter
public abstract class AbstractInvoiceItem {
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "tax_percent", nullable = false)
    private Double taxPercent;
    @Column(name = "promotion_amount", nullable = false)
    private Double promotionAmount;

    public Integer getQuantity() {
        return quantity == null ? 0 : quantity;
    }
    public Double getPrice() {
        return price == null ? 0.0 : price;
    }
    public Double getTaxPercent() {
        return taxPercent == null ? 0.0 : taxPercent;
    }
    public Double getPromotionAmount() {
        return promotionAmount == null ? 0.0 : promotionAmount;
    }

    public Double getTotalPrice() {
        return getQuantity() * getPrice();
    }
    public Double getTaxAmount() {
        return taxPercent == null ? 0.0 : taxPercent*quantity*price;
    }

}