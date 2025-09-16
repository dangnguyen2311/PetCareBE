package org.example.petcarebe.dto.request.discount;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateDiscountRequest {
    private String code;
    private String description;
    private Double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double maxAmount;
    private Double minAmount;
    private String status;
}

