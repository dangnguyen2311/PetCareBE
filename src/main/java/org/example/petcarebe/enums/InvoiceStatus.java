package org.example.petcarebe.enums;

public enum InvoiceStatus {
    DRAFT("Draft", "Invoice is being prepared"),
    PENDING("Pending", "Invoice is pending payment"),
    PAID("Paid", "Invoice has been fully paid"),
    PARTIALLY_PAID("Partially Paid", "Invoice has been partially paid"),
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

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
