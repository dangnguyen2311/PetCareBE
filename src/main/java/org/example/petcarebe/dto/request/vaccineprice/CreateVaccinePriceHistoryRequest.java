package org.example.petcarebe.dto.request.vaccineprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVaccinePriceHistoryRequest {
    private Long vaccineId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
}
