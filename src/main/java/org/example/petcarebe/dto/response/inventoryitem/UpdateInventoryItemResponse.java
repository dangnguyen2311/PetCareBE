package org.example.petcarebe.dto.response.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InventoryObjectType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInventoryItemResponse {
    private Long id;
    private String name;
    private Integer minQuantity;
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String inventoryObjectName;
    private InventoryObjectType inventoryObjectType;
    private String message;
}
