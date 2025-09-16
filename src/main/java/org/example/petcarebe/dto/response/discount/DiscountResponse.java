package org.example.petcarebe.dto.response.discount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {
    private Long id;
    private String code;
    private String description;
    private Double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double maxAmount;
    private Double minAmount;
    private String status;
}

