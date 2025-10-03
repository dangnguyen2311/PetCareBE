package org.example.petcarebe.enums;

import lombok.Getter;

@Getter
public enum InventoryItemType
{
    MEDICINE("Medicine", "Pharmaceutical drugs and medications"),
    PRODUCT("Product", "Pet care products and accessories"),
    VACCINE("Vaccine", "Vaccines for disease prevention");

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
