package org.example.petcarebe.enums;

public enum MedicinePriceHistoryEnum {
    ACTIVE("Active", "Current active price"),
    INACTIVE("Inactive", "Price is no longer active"),
    PENDING("Pending", "Price change is pending approval"),
    EXPIRED("Expired", "Price has expired"),
    SCHEDULED("Scheduled", "Price is scheduled for future activation");

    private final String displayName;
    private final String description;

    MedicinePriceHistoryEnum(String displayName, String description) {
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
