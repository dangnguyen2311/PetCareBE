package org.example.petcarebe.enums;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    DRAFT("Draft", "Invoice is being prepared"),
    PENDING("Pending", "Invoice is pending payment"),
    PAID("Paid", "Invoice has been fully paid"),
    PARTIALLYPAID("Partially Paid", "Invoice has been partially paid"),
    OVERDUE("Overdue", "Invoice payment is overdue"),
    CANCELLED("Cancelled", "Invoice has been cancelled"),
    REFUNDED("Refunded", "Invoice has been refunded"),
    VOID("Void", "Invoice has been voided");

    private final String displayName;
    private final String description;

    InvoiceStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
