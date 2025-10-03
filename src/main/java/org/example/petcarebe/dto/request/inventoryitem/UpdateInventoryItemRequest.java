package org.example.petcarebe.dto.request.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInventoryItemRequest {
    private String name;
    private Integer minQuantity;
    private String unit;

}
