package org.example.petcarebe.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
import org.example.petcarebe.dto.request.payment.PaymentSearchRequest;
import org.example.petcarebe.dto.request.payment.UpdatePaymentStatusRequest;
import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentStatisticsResponse;
import org.example.petcarebe.service.PaymentService;
import org.example.petcarebe.service.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/v1/payments")
@Tag(name = "💳 Payment Management", description = "Endpoints for managing payments (User & Admin)")
public class PaymentController {

    @Autowired
    private VnpayService vnpayService;

    @Autowired
    private PaymentService paymentService;

    @Operation(
            summary = "Create new payment",
            description = "Create a new payment for an invoice"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CreatePaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            HttpServletRequest httpRequest) {
        try {
            CreatePaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            CreatePaymentResponse errorResponse = CreatePaymentResponse.builder()
                    .success(false)
                    .message("Error creating payment: " + e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(
            summary = "Create VNPay payment (Legacy)",
            description = "Legacy endpoint for creating VNPay payment URL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment URL created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create-payment")
    public ResponseEntity<?> createVnpayPayment(
            @RequestBody PaymentRequestDto paymentRequest,
            HttpServletRequest request) {
        try {
            String paymentUrl = vnpayService.createPaymentUrl(paymentRequest, request);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Get my payments",
            description = "Get all payments for the current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @Parameter(description = "Customer ID", example = "1", required = true)
            @RequestParam Long customerId) {
        // In a real application, you would get the customer ID from the authenticated user
        List<PaymentResponse> payments = paymentService.getPaymentsByCustomerId(customerId);
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "Get payment details",
            description = "Get details of a specific payment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(
            @Parameter(description = "ID of the payment", example = "1", required = true)
            @PathVariable Long id) {
        try {
            PaymentResponse payment = paymentService.getPaymentById(id);
            // In a real application, you should verify that the payment belongs to the current user
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Check payment status",
            description = "Check the current status of a payment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(
            @Parameter(description = "ID of the payment", example = "1", required = true)
            @PathVariable Long id) {
        try {
            PaymentResponse payment = paymentService.getPaymentById(id);
            Map<String, Object> status = Map.of(
                "paymentId", payment.getId(),
                "status", payment.getStatus(),
                "statusDisplayName", payment.getStatusDisplayName(),
                "transactionCode", payment.getTransactionCode(),
                "amount", payment.getAmount()
            );
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "VNPay callback",
            description = "Handle callback from VNPay payment gateway"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Callback processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid callback data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/vnpay-callback")
    public ResponseEntity<Map<String, Object>> vnpayCallback(
            @RequestParam Map<String, String> vnpayParams) {
        try {
            PaymentResponse payment = paymentService.processVnpayCallback(vnpayParams);

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Payment processed successfully",
                "payment", payment
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Error processing payment: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(
            summary = "Get payment by transaction code",
            description = "Find a payment by its transaction code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/transaction/{transactionCode}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionCode(
            @Parameter(description = "Transaction code", example = "PAY_1234567890_ABCD1234", required = true)
            @PathVariable String transactionCode) {
        try {
            PaymentResponse payment = paymentService.getPaymentByTransactionCode(transactionCode);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== ADMIN ENDPOINTS ====================

    @Operation(
            summary = "[ADMIN] Get all payments",
            description = "Admin: Retrieve all payments in the system"
    )
    @GetMapping("/admin/all")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "[ADMIN] Update payment status",
            description = "Admin: Update the status of a payment"
    )
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @Parameter(description = "ID of the payment", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        try {
            PaymentResponse updatedPayment = paymentService.updatePaymentStatus(id, request);
            return ResponseEntity.ok(updatedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "[ADMIN] Get payments by invoice",
            description = "Admin: Retrieve all payments for a specific invoice"
    )
    @GetMapping("/admin/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByInvoice(
            @Parameter(description = "ID of the invoice", example = "1", required = true)
            @PathVariable Long invoiceId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByInvoiceId(invoiceId);
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "[ADMIN] Search payments",
            description = "Admin: Search payments with multiple criteria and pagination"
    )
    @PostMapping("/admin/search")
    public ResponseEntity<Page<PaymentResponse>> searchPayments(
            @Valid @RequestBody PaymentSearchRequest request) {
        Page<PaymentResponse> payments = paymentService.searchPayments(request);
        return ResponseEntity.ok(payments);
    }

    @Operation(
            summary = "[ADMIN] Get payment statistics",
            description = "Admin: Get comprehensive payment statistics for a date range"
    )
    @GetMapping("/admin/statistics")
    public ResponseEntity<PaymentStatisticsResponse> getPaymentStatistics(
            @Parameter(description = "Start date for statistics", example = "2024-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date for statistics", example = "2024-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        PaymentStatisticsResponse statistics = paymentService.getPaymentStatistics(fromDate, toDate);
        return ResponseEntity.ok(statistics);
    }

    @Operation(
            summary = "[ADMIN] Get payments by customer",
            description = "Admin: Retrieve all payments for a specific customer"
    )
    @GetMapping("/admin/customer/{customerId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByCustomerAdmin(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByCustomerId(customerId);
        return ResponseEntity.ok(payments);
    }
}

