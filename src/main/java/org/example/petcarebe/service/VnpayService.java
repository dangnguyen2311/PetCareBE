package org.example.petcarebe.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.petcarebe.config.VNPAYConfig;
import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
import org.example.petcarebe.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

//@Service
//public class VnpayService {
//
//    @Autowired
//    private VNPAYConfig vnpayConfig;
//
//    public String createPaymentUrl(PaymentRequestDto paymentRequest, HttpServletRequest request) {
//        System.out.println("=== Creating VNPay Payment URL ===");
//
//        long amount = paymentRequest.getAmount() * 100;
//        String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
//        String vnp_IpAddr = VNPayUtil.getIpAddress(request);
//
//        System.out.println("Amount: " + amount);
//        System.out.println("TxnRef: " + vnp_TxnRef);
//        System.out.println("IP Address (converted): " + vnp_IpAddr);
//
//        // Validate IP is IPv4
//        if (vnp_IpAddr.contains(":")) {
//            System.err.println("WARNING: IP address still contains ':' - " + vnp_IpAddr);
//            vnp_IpAddr = "127.0.0.1";
//            System.out.println("IP Address (forced): " + vnp_IpAddr);
//        }
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", "2.1.0");
//        vnp_Params.put("vnp_Command", "pay");
//        vnp_Params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", paymentRequest.getOrderInfo());
//        vnp_Params.put("vnp_OrderType", "other");
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        // Create query URL with encoded values
//        String queryUrl = VNPayUtil.getPaymentUrl(vnp_Params, true);
//
//        // Create hash data WITHOUT encoding (important for signature verification)
//        String hashData = VNPayUtil.getPaymentUrl(vnp_Params, false);
//        System.out.println("Hash Data: " + hashData);
//
//        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getVnp_HashSecret(), hashData);
//        System.out.println("Signature: " + vnp_SecureHash);
//        System.out.println("Secret Key: " + vnpayConfig.getVnp_HashSecret());
//
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//
//        String fullUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
//        System.out.println("Full Payment URL: " + fullUrl);
//        System.out.println("=== End Creating Payment URL ===\n");
//
//        return fullUrl;
//    }
//
//    /**
//     * Get secret key for signature verification
//     */
//    public String getSecretKey() {
//        return vnpayConfig.getVnp_HashSecret();
//    }
//}
//
