package org.example.petcarebe.dto.request.productprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductPriceHistoryRequest {
    private Long productId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
}
