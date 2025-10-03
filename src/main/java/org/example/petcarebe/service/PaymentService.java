package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.request.payment.PaymentSearchRequest;
import org.example.petcarebe.dto.request.payment.UpdatePaymentStatusRequest;
import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentStatisticsResponse;
import org.example.petcarebe.enums.PaymentStatus;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Payment;
import org.example.petcarebe.repository.InvoiceRepository;
import org.example.petcarebe.repository.PaymentRepository;
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

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private VnpayService vnpayService;

    /**
     * Create a new payment
     */
    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        // Validate invoice exists
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + request.getInvoiceId()));

        // Generate transaction code
        String transactionCode = generateTransactionCode();

        // Create payment
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setPaymentDate(LocalDate.now());
        payment.setTransactionCode(transactionCode);
        payment.setDescription(request.getDescription());
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        // Handle different payment methods
        String paymentUrl = null;
        if ("VNPAY".equals(request.getMethod())) {
            // Create VNPay payment URL
            paymentUrl = vnpayService.createPaymentUrl(
                convertToVnpayRequest(request, transactionCode),
                null // HttpServletRequest will be handled in controller
            );
        }

        return CreatePaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .transactionCode(transactionCode)
                .paymentUrl(paymentUrl)
                .message("Payment created successfully")
                .success(true)
                .build();
    }

    /**
     * Get payment by ID
     */
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return convertToResponse(payment);
    }

    /**
     * Get all payments
     */
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get payments by customer ID
     */
    public List<PaymentResponse> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get payments by invoice ID
     */
    public List<PaymentResponse> getPaymentsByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update payment status
     */
    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        payment.setStatus(request.getStatus());
        if (request.getReason() != null) {
            payment.setDescription(payment.getDescription() + " | Status change reason: " + request.getReason());
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return convertToResponse(updatedPayment);
    }



    /**
     * Search payments with criteria
     */
    public Page<PaymentResponse> searchPayments(PaymentSearchRequest request) {
        Sort sort = Sort.by(
            "DESC".equals(request.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
            request.getSortBy()
        );

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Payment> payments = paymentRepository.searchPayments(
            request.getStatus(),
            request.getMethod(),
            request.getTransactionCode(),
            request.getInvoiceId(),
            request.getFromDate(),
            request.getToDate(),
            request.getMinAmount(),
            request.getMaxAmount(),
            pageable
        );

        return payments.map(this::convertToResponse);
    }

    /**
     * Get payment statistics
     */
    public PaymentStatisticsResponse getPaymentStatistics(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) fromDate = LocalDate.now().minusMonths(1);
        if (toDate == null) toDate = LocalDate.now();

        // Overall statistics
        Long totalPayments = paymentRepository.count();
        Double totalAmount = paymentRepository.sumAmountByStatus(PaymentStatus.SUCCESS);
        if (totalAmount == null) totalAmount = 0.0;

        // Status breakdown
        Long successfulPayments = paymentRepository.countByStatus(PaymentStatus.SUCCESS);
        Long pendingPayments = paymentRepository.countByStatus(PaymentStatus.PENDING);
        Long failedPayments = paymentRepository.countByStatus(PaymentStatus.FAILED);

        // Amount breakdown
        Double successfulAmount = paymentRepository.sumAmountByStatus(PaymentStatus.SUCCESS);
        Double pendingAmount = paymentRepository.sumAmountByStatus(PaymentStatus.PENDING);
        Double failedAmount = paymentRepository.sumAmountByStatus(PaymentStatus.FAILED);

        // Method breakdown
        Map<String, Long> paymentsByMethod = new HashMap<>();
        Map<String, Double> amountByMethod = new HashMap<>();

        List<Object[]> methodCounts = paymentRepository.countByMethod();
        for (Object[] row : methodCounts) {
            paymentsByMethod.put((String) row[0], (Long) row[1]);
        }

        List<Object[]> methodAmounts = paymentRepository.sumAmountByMethod();
        for (Object[] row : methodAmounts) {
            amountByMethod.put((String) row[0], (Double) row[1]);
        }

        // Daily statistics
        Map<LocalDate, Double> dailyRevenue = new HashMap<>();
        Map<LocalDate, Long> dailyTransactions = new HashMap<>();

        List<Object[]> dailyRevenueData = paymentRepository.getDailyRevenue(fromDate, toDate);
        for (Object[] row : dailyRevenueData) {
            dailyRevenue.put((LocalDate) row[0], (Double) row[1]);
        }

        List<Object[]> dailyTransactionData = paymentRepository.getDailyTransactionCount(fromDate, toDate);
        for (Object[] row : dailyTransactionData) {
            dailyTransactions.put((LocalDate) row[0], (Long) row[1]);
        }

        return PaymentStatisticsResponse.builder()
                .totalPayments(totalPayments)
                .totalAmount(totalAmount)
                .totalRefunded(0.0)
                .netRevenue(totalAmount)
                .successfulPayments(successfulPayments)
                .pendingPayments(pendingPayments)
                .failedPayments(failedPayments)
                .refundedPayments(0L)
                .successfulAmount(successfulAmount)
                .pendingAmount(pendingAmount)
                .failedAmount(failedAmount)
                .paymentsByMethod(paymentsByMethod)
                .amountByMethod(amountByMethod)
                .fromDate(fromDate)
                .toDate(toDate)
                .dailyRevenue(dailyRevenue)
                .dailyTransactions(dailyTransactions)
                .build();
    }

    /**
     * Find payment by transaction code
     */
    public PaymentResponse getPaymentByTransactionCode(String transactionCode) {
        Payment payment = paymentRepository.findByTransactionCode(transactionCode)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction code: " + transactionCode));
        return convertToResponse(payment);
    }

    /**
     * Process VNPay callback
     */
    @Transactional
    public PaymentResponse processVnpayCallback(Map<String, String> vnpayParams) {
        String transactionCode = vnpayParams.get("vnp_TxnRef");
        String responseCode = vnpayParams.get("vnp_ResponseCode");

        Payment payment = paymentRepository.findByTransactionCode(transactionCode)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction code: " + transactionCode));

        if ("00".equals(responseCode)) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return convertToResponse(updatedPayment);
    }

    // ==================== HELPER METHODS ====================

    private String generateTransactionCode() {
        return "PAY_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private org.example.petcarebe.dto.request.payment.PaymentRequestDto convertToVnpayRequest(
            CreatePaymentRequest request, String transactionCode) {
        org.example.petcarebe.dto.request.payment.PaymentRequestDto vnpayRequest =
            new org.example.petcarebe.dto.request.payment.PaymentRequestDto();
        vnpayRequest.setAmount(request.getAmount().longValue());
        vnpayRequest.setOrderInfo(request.getDescription() != null ? request.getDescription() : "Payment for invoice " + request.getInvoiceId());
        return vnpayRequest;
    }

    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse.PaymentResponseBuilder builder = PaymentResponse.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .statusDisplayName(payment.getStatus().getDisplayName())
                .transactionCode(payment.getTransactionCode())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt());

        // Add invoice information if available
        if (payment.getInvoice() != null) {
            builder.invoiceId(payment.getInvoice().getId());
            // Add customer information if available
            if (payment.getInvoice().getCustomer() != null) {
                builder.customerId(payment.getInvoice().getCustomer().getId())
                       .customerName(payment.getInvoice().getCustomer().getFullname())
                       .customerEmail(payment.getInvoice().getCustomer().getEmail());
            }
        }

        return builder.build();
    }
}

