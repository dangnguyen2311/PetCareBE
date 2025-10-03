package org.example.petcarebe.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.PaymentStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchRequest {
    
    private PaymentStatus status;
    private String method;
    private String transactionCode;
    private Long invoiceId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double minAmount;
    private Double maxAmount;
    
    // Pagination
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "paymentDate";
    private String sortDirection = "DESC"; // ASC or DESC
}
