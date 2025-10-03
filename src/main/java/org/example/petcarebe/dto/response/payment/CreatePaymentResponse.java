package org.example.petcarebe.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentResponse {
    private Long paymentId;
    private String transactionCode;
    private String paymentUrl; // For online payments like VNPay
    private String qrCode; // For QR code payments
    private String message;
    private Boolean success;
}
