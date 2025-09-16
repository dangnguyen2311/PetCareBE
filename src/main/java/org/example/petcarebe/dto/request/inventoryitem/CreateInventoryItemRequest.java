package org.example.petcarebe.dto.request.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInventoryItemRequest {
    private Integer quantity;
    private String name;
    private Integer minQuantity;
    private String unit;
    private Long inventoryObjectId;
}
