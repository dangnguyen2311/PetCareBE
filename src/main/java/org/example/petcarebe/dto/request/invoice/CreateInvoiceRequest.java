package org.example.petcarebe.dto.request.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInvoiceRequest {
    private Long staffId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String notes;
    private LocalDate createdDate;
}
