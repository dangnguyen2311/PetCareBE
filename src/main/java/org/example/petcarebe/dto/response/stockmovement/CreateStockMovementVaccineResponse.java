package org.example.petcarebe.dto.response.stockmovement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStockMovementVaccineResponse {
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
    private String vaccineName;
    private String vaccineDescription;
    private String vaccineManufacturer;
    private String message;
}
