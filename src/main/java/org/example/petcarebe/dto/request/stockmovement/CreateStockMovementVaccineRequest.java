package org.example.petcarebe.dto.request.stockmovement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStockMovementVaccineRequest {
    private Integer quantity;
    private Double price;
    private LocalDateTime movementDate;
    private String notes;
    private Long vaccineId;
}
