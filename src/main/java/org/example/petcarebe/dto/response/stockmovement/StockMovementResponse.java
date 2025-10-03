package org.example.petcarebe.dto.response.stockmovement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InventoryItemType;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementResponse {
    private Long id;
    private StockMovementType movementType; //IN, OUT, PURCHASE, SALE, RETURN, ADJUSTMENT, TRANSFER, EXPIRED, DAMAGED, LOST, PRESCRIPTION, TREATMENT
    private Integer quantity;
    private Double price;
    private LocalDateTime movementDate;
    private LocalDate createdAt;
    private String notes;
    private Long inventoryItemId;
    private String inventoryItemName;
    private InventoryItemType inventoryItemType;// PRODUCT, VACCINE, MEDICINE
    private String message;
}
