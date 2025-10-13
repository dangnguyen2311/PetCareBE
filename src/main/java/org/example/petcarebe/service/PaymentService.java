package org.example.petcarebe.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.petcarebe.config.VNPAYConfig;
import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.request.payment.PaymentSearchRequest;
import org.example.petcarebe.dto.request.payment.UpdatePaymentStatusRequest;
import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentStatisticsResponse;
import org.example.petcarebe.enums.PaymentStatus;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Payment;
import org.example.petcarebe.payment.PaymentDTO;
import org.example.petcarebe.payment.PaymentRequest;
import org.example.petcarebe.payment.PaymentRequestRepository;
import org.example.petcarebe.repository.InvoiceRepository;
import org.example.petcarebe.repository.PaymentRepository;
import org.example.petcarebe.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//    private final VNPAYConfig vnPayConfig;
//
//    @Autowired
//    private PaymentRequestRepository paymentRepository;
//
//    public PaymentDTO.VNPayResponse createVnPayPayment(long amount, String bankCode, String orderNote, HttpServletRequest request, String returnUrl) {
//        //amount = Integer.parseInt(request.getParameter("amount")) * 100L;
//        //String bankCode = request.getParameter("bankCode");
//        //String orderNote = request.getParameter("orderNote");
//        //String returnUrl = request.getParameter("returnUrl");
//
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Amount must be greater than 0");
//        }
//        if (orderNote == null || orderNote.isEmpty()) {
//            throw new IllegalArgumentException("Order note cannot be empty");
//        }
////        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
////        String bankCode = request.getParameter("bankCode");
//        amount = amount*100L;
//
//        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(returnUrl);
//        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnpParamsMap.put("vnp_BankCode", bankCode);
//        }
//
//        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
//
////        vnpParamsMap.put("vnp_OrderInfo", orderNote);
//        vnpParamsMap.replace("vnp_OrderNote", orderNote);
//        //build query url
//        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
//        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
//        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
//        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//        return PaymentDTO.VNPayResponse.builder()
//                .code("ok")
//                .message("success")
//                .paymentUrl(paymentUrl).build();
//    }
//
//    public void savePaymentRequest(PaymentRequest paymentRequest) {
//        paymentRepository.save(paymentRequest);
//    }
//    public void updatePaymentRequest(PaymentRequest paymentRequest) {
//        paymentRepository.save(paymentRequest);
//    }
//}