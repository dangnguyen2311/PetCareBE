package org.example.petcarebe.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
import org.example.petcarebe.dto.request.payment.PaymentSearchRequest;
import org.example.petcarebe.dto.request.payment.UpdatePaymentStatusRequest;
import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentStatisticsResponse;
import org.example.petcarebe.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping("/vn-pay/{invoiceId}")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(@RequestBody CreatePaymentRequest paymentRequest,
                                                        @PathVariable(value = "invoiceId") Long invoiceId,
                                                        HttpServletRequest request) {
//        paymentService.savePaymentRequest(paymentRequest);
        paymentService.createPaymentForInvoice(invoiceId, paymentRequest);
        return new ResponseObject<>(HttpStatus.OK, "Successssssss",
                paymentService.createVnPayPayment(
//                        paymentRequest.getAmount(),
//                        paymentRequest.getBankCode(),
//                        paymentRequest.getNotes(),
//                        request,
//                        paymentRequest.getReturnUrl(),
                        paymentRequest, request
        ));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<ResponseObject<PaymentDTO.VNPayResponse>> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");

        if ("00".equals(status)) {
            System.out.println("thanh toan thanh cpng");
            PaymentDTO.VNPayResponse response = PaymentDTO.VNPayResponse.builder()
                    .code("00")
                    .message("Success")
                    .paymentUrl("")
                    .build();
            //Update invoice status in DB

            return ResponseEntity.ok(new ResponseObject<>(HttpStatus.OK, "Success", response));
        } else {
            return ResponseEntity.badRequest().body(
                    new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null));
        }
    }


//    @GetMapping("/test-encoding")
//    public ResponseEntity<?> testEncoding() {
//        java.util.Map<String, String> params = new java.util.HashMap<>();
//        params.put("vnp_Amount", "20000000");
//        params.put("vnp_OrderInfo", "Thanh toan don hang #HD12345");
//        params.put("vnp_TmnCode", "1FO325BY");
//
//        String hashData = org.example.petcarebe.util.VNPayUtil.getPaymentURL(params, false);
//        String queryUrl = org.example.petcarebe.util.VNPayUtil.getPaymentURL(params, true);
//        String signature = org.example.petcarebe.util.VNPayUtil.hmacSHA512("X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA", hashData);
//
//        java.util.Map<String, Object> result = new java.util.HashMap<>();
//        result.put("hashData", hashData);
//        result.put("queryUrl", queryUrl);
//        result.put("signature", signature);
//        result.put("signatureLength", signature.length());
//
//        return ResponseEntity.ok(result);
//    }
}

