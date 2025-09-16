package org.example.petcarebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
import org.example.petcarebe.service.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/v1/payments")
public class PaymentController {

    @Autowired
    private VnpayService vnpayService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto paymentRequest, HttpServletRequest request) {
        String paymentUrl = vnpayService.createPaymentUrl(paymentRequest, request);
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }
}

