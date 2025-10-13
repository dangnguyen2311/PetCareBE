package org.example.petcarebe.dto.response.stockmovement;

import lombok.*;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStockMovementMedicineResponse {
    private Long id;
    private StockMovementType movementType;
    private Integer quantity;
    private Double price;
    private LocalDateTime movementDate;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long inventoryItemId;
    private String inventoryItemName;
    private String inventoryItemUnit;
    private String medicineName;
    private String medicineUnit;
    private String medicineDescription;
    private String message;
}
