package org.example.petcarebe.dto.response.promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Long id;
    private String name;
    private String type;
    private Double value;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}

