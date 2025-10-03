package org.example.petcarebe.dto.response.medicineprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicinePriceHistoryResponse {
    private Long id;
    private Long medicineid;
    private String medicineName;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    // ACTIVE, INACTIVE
    private String status;
    private String message;
}
