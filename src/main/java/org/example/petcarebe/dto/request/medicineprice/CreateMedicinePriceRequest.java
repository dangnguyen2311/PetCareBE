package org.example.petcarebe.dto.request.medicineprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedicinePriceRequest {
    private Long medicineId;
    private LocalDate startDate;
    private Double price;

}
