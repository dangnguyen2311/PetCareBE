package org.example.petcarebe.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private LocalDate paymentDate;
    private Double amount;
    private String method;
    private PaymentStatus status; //PENDING, PROCESSING, SUCCESS, FAILED, CANCELLED, REFUNDED, PARTIAL_REFUND, EXPIRED, DECLINED
    private String statusDisplayName;
    private String transactionCode;
    
    // Invoice information
    private Long invoiceId;
    
    // Additional fields
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String messgae;
}
