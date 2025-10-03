package org.example.petcarebe.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePaymentStatusRequest {
    
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
    
    private String reason; // Reason for status change
    
    private String adminNote; // Admin note for the status change
}
