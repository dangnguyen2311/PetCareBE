package org.example.petcarebe.dto.response.vaccineprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccinePriceHistoryResponse {
    private Long id;
    private Long vaccineId;
    private String vaccineName;
    private String vaccineManufacturer;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    // ACTIVE, INACTIVE
    private String status;
    private String message;
}
