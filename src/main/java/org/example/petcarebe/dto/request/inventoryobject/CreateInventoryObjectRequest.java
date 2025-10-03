package org.example.petcarebe.dto.request.inventoryobject;

import lombok.Data;
import org.example.petcarebe.enums.InventoryObjectType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateInventoryObjectRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Type is required")
    private InventoryObjectType type;
}
