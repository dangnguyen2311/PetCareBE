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
public class InvoiceResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private LocalDate createdDate;
    private Double totalAmount;
    private Double totalDiscountAmount;
    private Double taxTotalAmount;
    private Double finalAmount;
    private InvoiceStatus status;
    private String notes;
    private Long staffId;
    private String staffName;
    private String message;

}
