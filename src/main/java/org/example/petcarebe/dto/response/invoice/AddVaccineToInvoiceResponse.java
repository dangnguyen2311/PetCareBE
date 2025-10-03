package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddVaccineToInvoiceResponse {
    private Long invoiceId;
    private String vaccineName;
    private String vaccineDescription;
    private String vaccineManufacturer;
    private Integer quantity;
    private Double taxPercent;
    private Double promotionAmount;
    private String notes;
    private String message;
}
