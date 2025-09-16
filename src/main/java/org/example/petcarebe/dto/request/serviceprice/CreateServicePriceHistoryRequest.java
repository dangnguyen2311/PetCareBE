package org.example.petcarebe.dto.request.serviceprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServicePriceHistoryRequest {
    private Long serviceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;

}
