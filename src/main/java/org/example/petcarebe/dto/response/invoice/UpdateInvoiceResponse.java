package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.InvoiceStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInvoiceResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String notes;
    private LocalDate createDate;
    private InvoiceStatus status;
    private Long staffId;
    private Long customerId;
    private Double totalAmount;
    private Double totalDiscountAmount;
    private Double finalAmount;
    private String message;
}
