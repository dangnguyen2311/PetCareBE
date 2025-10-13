package org.example.petcarebe.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentSuccessRequest {
    private Long amount;
    private String bankCode;
    private String transactionCode;
    private String method;
}
