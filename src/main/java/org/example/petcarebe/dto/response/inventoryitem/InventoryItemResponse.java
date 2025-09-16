package org.example.petcarebe.dto.response.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemResponse {
    private Long id;
    private Integer quantity;
    private String name;
    private Integer minQuantity;
    private String unit;
    private LocalDate createdDate;
    private LocalDate endDate;
    private Long inventoryObjectId;
}
