package org.example.petcarebe.dto.response.stockmovement;

import lombok.*;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStockMovementProductResponse {
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
    private Long productId;
    private String productName;
    private String productUnit;
    private String productCategory;
    private String productBrand;
    private String productSupplier;
    private String message;

}
