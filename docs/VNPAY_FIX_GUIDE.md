# 🔧 Hướng dẫn Fix Lỗi VNPay "Sai chữ ký"

## 📋 Vấn đề

Khi thực hiện thanh toán VNPay, gặp lỗi **"Sai chữ ký"** (Invalid Signature).

## 🔍 Nguyên nhân

### 1. **Encoding không đúng chuẩn VNPay**
Theo tài liệu VNPay, khi tạo chữ ký:
- **Hash Data (để tạo signature)**: KHÔNG encode key và value
- **Query URL (để gửi request)**: CHỈ encode value, KHÔNG encode key

**Lỗi cũ:**
```java
// Cả key và value đều bị encode
String queryUrl = VnpayUtil.getPaymentUrl(vnp_Params, true);  // encode cả key
String hashData = VnpayUtil.getPaymentUrl(vnp_Params, false); // không encode
```

**Fix:**
```java
// Chỉ encode value cho query URL
String queryUrl = VnpayUtil.getPaymentUrl(vnp_Params, true);  // encode value only
// Không encode gì cho hash data
String hashData = VnpayUtil.getPaymentUrl(vnp_Params, false); // no encoding
```

### 2. **Thiếu verify signature khi VNPay callback**
Endpoint `/vnpay-callback` không verify chữ ký từ VNPay trả về.

**Lỗi cũ:**
```java
@GetMapping("/vnpay-callback")
public ResponseEntity<?> vnpayCallback(@RequestParam Map<String, String> vnpayParams) {
    // Không verify signature
    PaymentResponse payment = paymentService.processVnpayCallback(vnpayParams);
    return ResponseEntity.ok(payment);
}
```

**Fix:**
```java
@GetMapping("/vnpay-callback")
public ResponseEntity<?> vnpayCallback(@RequestParam Map<String, String> vnpayParams) {
    String secretKey = vnpayService.getSecretKey();
    // Verify signature trước khi xử lý
    PaymentResponse payment = paymentService.processVnpayCallback(vnpayParams, secretKey);
    return ResponseEntity.ok(payment);
}
```

## ✅ Các thay đổi đã thực hiện

### 1. **VnpayUtil.java**
```java
// Thêm method verify signature
public static boolean verifyVnpaySignature(Map<String, String> params, String secretKey) {
    String vnpSecureHash = params.get("vnp_SecureHash");
    if (vnpSecureHash == null) {
        return false;
    }
    
    // Remove vnp_SecureHash from params before creating hash data
    Map<String, String> paramsWithoutHash = params.entrySet().stream()
            .filter(entry -> !entry.getKey().equals("vnp_SecureHash") && 
                           !entry.getKey().equals("vnp_SecureHashType"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    
    // Create hash data (no encoding)
    String hashData = getPaymentUrl(paramsWithoutHash, false);
    String calculatedHash = hmacSHA512(secretKey, hashData);
    
    return vnpSecureHash.equals(calculatedHash);
}

// Fix encoding logic
public static String getPaymentUrl(Map<String, String> params, boolean encodeValue) {
    return params.entrySet().stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> {
                String key = entry.getKey(); // KHÔNG encode key
                String value = encodeValue ? 
                    URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8) : 
                    entry.getValue();
                return key + "=" + value;
            })
            .collect(Collectors.joining("&"));
}
```

### 2. **PaymentService.java**
```java
@Transactional
public PaymentResponse processVnpayCallback(Map<String, String> vnpayParams, String secretKey) {
    // Verify signature first
    if (!VnpayUtil.verifyVnpaySignature(vnpayParams, secretKey)) {
        throw new RuntimeException("Invalid VNPay signature");
    }
    
    String transactionCode = vnpayParams.get("vnp_TxnRef");
    String responseCode = vnpayParams.get("vnp_ResponseCode");

    Payment payment = paymentRepository.findByTransactionCode(transactionCode)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

    if ("00".equals(responseCode)) {
        payment.setStatus(PaymentStatus.SUCCESS);
        
        // Update invoice status
        if (payment.getInvoice() != null) {
            Invoice invoice = payment.getInvoice();
            invoice.setStatus(InvoiceStatus.PAID);
            invoiceRepository.save(invoice);
        }
    } else {
        payment.setStatus(PaymentStatus.FAILED);
    }

    return convertToResponse(paymentRepository.save(payment));
}
```

### 3. **PaymentController.java**
```java
@GetMapping("/vnpay-callback")
public ResponseEntity<Map<String, Object>> vnpayCallback(
        @RequestParam Map<String, String> vnpayParams) {
    try {
        // Log parameters for debugging
        System.out.println("=== VNPay Callback Parameters ===");
        vnpayParams.forEach((key, value) -> 
            System.out.println(key + " = " + value)
        );
        
        // Get secret key and verify
        String secretKey = vnpayService.getSecretKey();
        PaymentResponse payment = paymentService.processVnpayCallback(vnpayParams, secretKey);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Payment processed successfully",
            "payment", payment
        ));
    } catch (RuntimeException e) {
        System.err.println("VNPay callback error: " + e.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "Error: " + e.getMessage()
        ));
    }
}
```

### 4. **VnpayService.java**
```java
public String createPaymentUrl(PaymentRequestDto paymentRequest, HttpServletRequest request) {
    // ... setup params ...
    
    // Create query URL with encoded values
    String queryUrl = VnpayUtil.getPaymentUrl(vnp_Params, true);
    
    // Create hash data WITHOUT encoding (important!)
    String hashData = VnpayUtil.getPaymentUrl(vnp_Params, false);
    String vnp_SecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
    
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    return vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
}

public String getSecretKey() {
    return vnpayConfig.getSecretKey();
}
```

## 🧪 Cách test

### 1. **Kiểm tra config trong .env**
```properties
# VNPay Sandbox
PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
TMN_CODE=1FO325BY
SECRET_KEY=X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA
RETURN_URL=http://localhost:5173/payment
VERSION=2.1.0
COMMAND=pay
ORDER_TYPE=other
```

### 2. **Chạy unit test**
```bash
mvn test -Dtest=VnpayUtilTest
```

### 3. **Test thanh toán thực tế**
1. Tạo payment request qua API
2. Click vào payment URL
3. Thanh toán trên VNPay sandbox
4. Kiểm tra logs trong console khi VNPay callback
5. Verify payment status được update đúng

## 📝 Checklist

- [x] Fix encoding logic trong `VnpayUtil.getPaymentUrl()`
- [x] Thêm method `verifyVnpaySignature()` trong `VnpayUtil`
- [x] Update `PaymentService.processVnpayCallback()` để verify signature
- [x] Update `PaymentController.vnpayCallback()` để pass secretKey
- [x] Thêm logging để debug
- [x] Tạo unit tests
- [x] Update invoice status khi payment success

## 🔗 Tài liệu tham khảo

- [VNPay API Documentation](https://sandbox.vnpayment.vn/apis/docs/huong-dan-tich-hop/)
- [VNPay Sandbox](https://sandbox.vnpayment.vn/)

## ⚠️ Lưu ý

1. **Secret Key phải khớp** với TMN Code trên VNPay
2. **Return URL** phải được đăng ký trên VNPay merchant portal
3. **Encoding**: UTF-8 cho tất cả parameters
4. **Sorting**: Parameters phải được sort theo alphabet trước khi tạo hash
5. **Hash Algorithm**: HMAC-SHA512 (lowercase hex output)

