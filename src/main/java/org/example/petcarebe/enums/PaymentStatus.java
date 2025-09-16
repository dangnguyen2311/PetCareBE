package org.example.petcarebe.enums;

public enum PaymentStatus {
    PENDING("Pending", "Payment is pending processing"),
    PROCESSING("Processing", "Payment is being processed"),
    SUCCESS("Success", "Payment completed successfully"),
    FAILED("Failed", "Payment failed"),
    CANCELLED("Cancelled", "Payment was cancelled"),
    REFUNDED("Refunded", "Payment has been refunded"),
    PARTIAL_REFUND("Partial Refund", "Payment has been partially refunded"),
    EXPIRED("Expired", "Payment session has expired"),
    DECLINED("Declined", "Payment was declined by bank/provider");

    private final String displayName;
    private final String description;

    PaymentStatus(String displayName, String description) {
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
