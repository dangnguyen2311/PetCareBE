package org.example.petcarebe.enums;

public enum StockMovementType {
    IN("In", "Stock received/added to inventory"),
    OUT("Out", "Stock removed/used from inventory"),
    PURCHASE("Purchase", "Stock received from supplier purchase"),
    SALE("Sale", "Stock sold to customer"),
    RETURN("Return", "Stock returned from customer"),
    ADJUSTMENT("Adjustment", "Stock quantity adjustment"),
    TRANSFER("Transfer", "Stock transferred between locations"),
    EXPIRED("Expired", "Stock removed due to expiration"),
    DAMAGED("Damaged", "Stock removed due to damage"),
    LOST("Lost", "Stock removed due to loss/theft"),
    PRESCRIPTION("Prescription", "Stock used for prescription fulfillment"),
    TREATMENT("Treatment", "Stock used during treatment/service");

    private final String displayName;
    private final String description;

    StockMovementType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInbound() {
        return this == IN || this == PURCHASE || this == RETURN || this == ADJUSTMENT;
    }

    public boolean isOutbound() {
        return this == OUT || this == SALE || this == EXPIRED || this == DAMAGED ||
               this == LOST || this == PRESCRIPTION || this == TREATMENT;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
