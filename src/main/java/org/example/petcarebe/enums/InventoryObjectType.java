package org.example.petcarebe.enums;

import lombok.Getter;

@Getter
public enum InventoryObjectType {
    MEDICINE("Medicine", "Pharmaceutical drugs and medications"),
    PRODUCT("Product", "Pet care products and accessories"),
    VACCINE("Vaccine", "Vaccines for disease prevention"),
    EQUIPMENT("Equipment", "Medical and clinic equipment"),
    SUPPLY("Supply", "General medical supplies and consumables");

    private final String displayName;
    private final String description;

    InventoryObjectType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}