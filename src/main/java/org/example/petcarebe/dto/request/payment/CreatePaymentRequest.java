package org.example.petcarebe.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequest {
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;
    
    @NotBlank(message = "Payment method is required")
    private String method; // VNPAY, CASH, BANK_TRANSFER, CREDIT_CARD
    private String bankCode;
    private String notes;
    
    private String returnUrl; // For online payments
}
