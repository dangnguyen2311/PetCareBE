package org.example.petcarebe.dto.response.vaccine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InventoryObjectType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccineResponse {
    private Long id;
    private String name;
    private String manufacturer;
    private String description;
    private Long inventoryObjectId;
    private InventoryObjectType inventoryObjectType;
    private String message;
}

