package org.example.petcarebe.payment;

import lombok.Builder;
import lombok.Data;

@Data
public abstract class PaymentDTO {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}