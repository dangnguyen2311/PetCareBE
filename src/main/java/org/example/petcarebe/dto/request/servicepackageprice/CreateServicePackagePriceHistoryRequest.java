package org.example.petcarebe.dto.request.servicepackageprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServicePackagePriceHistoryRequest {
    private Long servicePackageId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
}
