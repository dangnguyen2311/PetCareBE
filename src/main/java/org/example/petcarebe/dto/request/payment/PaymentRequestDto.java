package org.example.petcarebe.dto.request.payment;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private long amount;
    private String orderInfo;
}

