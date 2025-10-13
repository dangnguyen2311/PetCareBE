package org.example.petcarebe.enums;

import lombok.Getter;

@Getter
public enum InventoryItemType
{
    MEDICINE("MEDICINE", "Pharmaceutical drugs and medications"),
    PRODUCT("PRODUCT", "Pet care products and accessories"),
    VACCINE("VACCINE", "Vaccines for disease prevention");

    private final String displayName;
    private final String description;

    InventoryItemType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String toString() {
        return displayName + " " + description;
    }
}
