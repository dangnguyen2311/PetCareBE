package org.example.petcarebe.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentrequest")
@Entity
public class PaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the payment request

    @Column(name = "amount")
    private int amount;

    @Column(name = "bank_code")
    private String bankCode;
    private String note;
    @Column(name = "return_url")
    private String returnUrl;
}