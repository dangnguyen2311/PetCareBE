package org.example.petcarebe.dto.response.vaccine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineInInvoiceResponse {
    private Long id;
    private Double price;
    private Integer quantity;
    private Double taxAmount;
    private Double promotionAmount;
    private String notes;
    private Long vaccineId;
    private String vaccineName;
    private String vaccineManufacturer;
}
