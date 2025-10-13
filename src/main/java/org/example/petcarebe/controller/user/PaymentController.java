//package org.example.petcarebe.controller.user;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
//import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
//import org.example.petcarebe.dto.request.payment.PaymentSearchRequest;
//import org.example.petcarebe.dto.request.payment.UpdatePaymentStatusRequest;
//import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
//import org.example.petcarebe.dto.response.payment.PaymentResponse;
//import org.example.petcarebe.dto.response.payment.PaymentStatisticsResponse;
//import org.example.petcarebe.payment.PaymentDTO;
//import org.example.petcarebe.payment.PaymentService;
//import org.example.petcarebe.payment.ResponseObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/user/v1/payments")
//@Tag(name = "💳 Payment Management", description = "Endpoints for managing payments (User & Admin)")
//public class PaymentController {
//    @Autowired
//    private final PaymentService paymentService;
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private ReceiptService receiptService;
//    @Autowired
//    private ReturnOrderService returnOrderService;
//
//    @PostMapping("/vn-pay/{orderId}")
//    public ResponseObject<PaymentDTO.VNPayResponse> pay(@RequestBody PaymentRequest paymentRequest,
//                                                        @PathVariable(value = "orderId") String orderId,
//                                                        HttpServletRequest request) {
//        paymentService.savePaymentRequest(paymentRequest);
//
//        return new ResponseObject<>(HttpStatus.OK, "Successssssss", paymentService.createVnPayPayment(paymentRequest.getAmount(),
//                paymentRequest.getBankCode(),
//                paymentRequest.getNote(),
//                request,
//                paymentRequest.getReturnUrl()
//        ));
//    }
//    @GetMapping("/vn-pay-callback")
//    public ResponseEntity<ResponseObject<PaymentDTO.VNPayResponse>> payCallbackHandler(HttpServletRequest request) {
//        String status = request.getParameter("vnp_ResponseCode");
//
//        if ("00".equals(status)) {
//            PaymentDTO.VNPayResponse response = PaymentDTO.VNPayResponse.builder()
//                    .code("00")
//                    .message("Success")
//                    .paymentUrl("")
//                    .build();
//            //Update order status in DB
//
//            return ResponseEntity.ok(new ResponseObject<>(HttpStatus.OK, "Success", response));
//        } else {
//            return ResponseEntity.badRequest().body(
//                    new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null));
//        }
//    }
//
//}