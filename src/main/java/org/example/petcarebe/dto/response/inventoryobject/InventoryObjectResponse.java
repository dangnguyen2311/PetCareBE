package org.example.petcarebe.dto.response.inventoryobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InventoryObjectType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryObjectResponse {
    private Long id;
    private String name;
    private String description;
    private InventoryObjectType type;
    private String typeDisplayName;
    private String typeDescription;
}
