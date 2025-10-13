package org.example.petcarebe.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.petcarebe.config.VNPAYConfig;
import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.enums.PaymentStatus;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Payment;
import org.example.petcarebe.repository.InvoiceRepository;
import org.example.petcarebe.repository.PaymentRepository;
import org.example.petcarebe.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    public PaymentDTO.VNPayResponse createVnPayPayment(CreatePaymentRequest paymentRequest, HttpServletRequest request) {
        //amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        //String bankCode = request.getParameter("bankCode");
        //String orderNote = request.getParameter("orderNote");
        //String returnUrl = request.getParameter("returnUrl");
        Long amount = paymentRequest.getAmount();
        String notes = paymentRequest.getNotes();
        String bankCode = paymentRequest.getBankCode();
        String returnUrl = paymentRequest.getReturnUrl();

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
//        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
//        String bankCode = request.getParameter("bankCode");
        amount = amount*100L;

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(returnUrl);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

//        vnpParamsMap.put("vnp_OrderInfo", orderNote);
        vnpParamsMap.replace("vnp_OrderNote", notes);
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    public void savePaymentRequest(CreatePaymentRequest request) {
//        Payment payment = new Payment();
//        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
//                .orElseThrow(() -> new IllegalArgumentException("Invoice id " + request.getInvoiceId() + " not found"));
//        payment.setInvoice(invoice);
//        payment.setAmount(request.getAmount());
//        payment.setPaymentDate(LocalDate.now());
//        payment.setMethod(request.getMethod());
//        payment.setStatus();
//        payment.setTransactionCode();
//        payment.setDescription(request.getDescription());
//        payment.setCreatedAt(LocalDateTime.now());


    }
    public void updatePaymentRequest(PaymentRequest paymentRequest) {
        paymentRequestRepository.save(paymentRequest);
    }

    public PaymentResponse createPaymentForInvoice(Long invoiceId, CreatePaymentRequest request) {
        Payment payment = new Payment();
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice id " + invoiceId + " not found"));
        payment.setInvoice(invoice);
        payment.setAmount(Double.valueOf(request.getAmount()));
        payment.setPaymentDate(LocalDate.now());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionCode("");
        payment.setDescription(request.getNotes());
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToPaymentResponse(savedPayment, "Payment created with PENDING status");
    }

    private PaymentResponse convertToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .statusDisplayName(payment.getStatus().getDisplayName())
                .transactionCode(payment.getTransactionCode())
                .invoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null)
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .messgae("")
                .build();
    }

    private PaymentResponse convertToPaymentResponse(Payment payment, String message) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .statusDisplayName(payment.getStatus().getDisplayName())
                .transactionCode(payment.getTransactionCode())
                .invoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null)
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .messgae(message)
                .build();
    }
}