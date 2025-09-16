package org.example.petcarebe.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.petcarebe.config.VnpayConfig;
import org.example.petcarebe.dto.request.payment.PaymentRequestDto;
import org.example.petcarebe.util.VnpayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class VnpayService {

    @Autowired
    private VnpayConfig vnpayConfig;

    public String createPaymentUrl(PaymentRequestDto paymentRequest, HttpServletRequest request) {
        long amount = paymentRequest.getAmount() * 100;
        String vnp_TxnRef = VnpayUtil.getRandomNumber(8);
        String vnp_IpAddr = VnpayUtil.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", paymentRequest.getOrderInfo());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        String queryUrl = VnpayUtil.getPaymentUrl(vnp_Params, true);
        String hashData = VnpayUtil.getPaymentUrl(vnp_Params, false);
        String vnp_SecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getVnp_HashSecret(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }
}

