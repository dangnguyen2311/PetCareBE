package org.example.petcarebe.dto.response.inventoryitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInventoryItemResponse {
    private Long id;
    private Integer quantity;
    private String name;
    private Integer minQuantity;
    private String unit;
    private LocalDateTime createdAt;
    private Long inventoryObjectId;
    private String message;
}
