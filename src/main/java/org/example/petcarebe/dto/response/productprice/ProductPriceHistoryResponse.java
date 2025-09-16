package org.example.petcarebe.dto.response.productprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceHistoryResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    // ACTIVE, INACTIVE
    private String status;
    private String message;

}
