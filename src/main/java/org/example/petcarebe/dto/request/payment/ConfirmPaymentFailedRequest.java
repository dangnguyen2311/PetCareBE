package org.example.petcarebe.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.PaymentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentFailedRequest {
    private Long amount;
    private String bankCode;
    private String transactionCode;
    private String method;
    private PaymentStatus paymentStatus;
}
