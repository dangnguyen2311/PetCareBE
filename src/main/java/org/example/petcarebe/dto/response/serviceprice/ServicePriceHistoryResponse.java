package org.example.petcarebe.dto.response.serviceprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePriceHistoryResponse {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    // ACTIVE, INACTIVE
    private String status;
    private String message;
}
