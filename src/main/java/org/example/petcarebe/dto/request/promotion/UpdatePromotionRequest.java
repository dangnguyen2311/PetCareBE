package org.example.petcarebe.dto.request.promotion;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdatePromotionRequest {
    private String name;
    private String type;
    private Double value;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}

