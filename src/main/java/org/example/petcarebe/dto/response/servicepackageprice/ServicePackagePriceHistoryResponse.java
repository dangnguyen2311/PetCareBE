package org.example.petcarebe.dto.response.servicepackageprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePackagePriceHistoryResponse {
    private Long id;
    private Long servicePackageId;
    private String servicePackageName;
    private String servicePackageDescription;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    // ACTIVE, INACTIVE
    private String status;
    private String message;
}
