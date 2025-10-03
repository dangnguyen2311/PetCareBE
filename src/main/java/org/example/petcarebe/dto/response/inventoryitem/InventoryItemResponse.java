package org.example.petcarebe.dto.response.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InventoryObjectType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemResponse {
    private Long id;
    private Integer quantity;
    private String name;
    private Integer minQuantity;
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long inventoryObjectId;
    private String inventoryObjectName;
    private String inventoryObjectDescription;
    private InventoryObjectType inventoryObjectType;
    private String message;
}
