# 🏦 VNPay Error: "Ngân hàng thanh toán không được hỗ trợ"

## 🐛 Lỗi

```
Thông báo: Ngân hàng thanh toán không được hỗ trợ
Mã tra cứu: Zf0JWgdgWT
Thời gian giao dịch: 07/10/2025 11:14:36 CH
```

## 🔍 Nguyên nhân

Lỗi này **KHÔNG phải** do ngân hàng thực sự không được hỗ trợ, mà do:

### 1. **Parameter không hợp lệ: `vnp_OrderNote`**

**Code cũ (SAI):**
```java
vnpParamsMap.replace("vnp_OrderNote", orderNote);
```

**Vấn đề:**
- `vnp_OrderNote` KHÔNG phải là parameter hợp lệ của VNPay API
- VNPay chỉ chấp nhận: `vnp_OrderInfo`
- Khi có parameter không hợp lệ → VNPay trả về lỗi generic "Ngân hàng không được hỗ trợ"

**Fix:**
```java
vnpParamsMap.put("vnp_OrderInfo", orderNote);
```

### 2. **IPv6 Address**

**Code cũ:**
```java
vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
// → Có thể trả về: 0:0:0:0:0:0:0:1 (IPv6)
```

**Vấn đề:**
- VNPay chỉ chấp nhận IPv4
- IPv6 sẽ bị reject

**Fix:**
```java
String ipAddress = VNPayUtil.getIpAddress(request);
if (ipAddress.contains(":")) {
    ipAddress = "127.0.0.1"; // Convert IPv6 to IPv4
}
vnpParamsMap.put("vnp_IpAddr", ipAddress);
```

## ✅ Giải pháp đầy đủ

### File: `PaymentService.java`

```java
public PaymentDTO.VNPayResponse createVnPayPayment(long amount, String bankCode, String orderNote, HttpServletRequest request, String returnUrl) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be greater than 0");
    }
    if (orderNote == null || orderNote.isEmpty()) {
        throw new IllegalArgumentException("Order note cannot be empty");
    }
    
    amount = amount * 100L;

    Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(returnUrl);
    vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
    
    // Add bank code if provided
    if (bankCode != null && !bankCode.isEmpty()) {
        vnpParamsMap.put("vnp_BankCode", bankCode);
    }

    // Get IP address and convert IPv6 to IPv4
    String ipAddress = VNPayUtil.getIpAddress(request);
    if (ipAddress.contains(":")) {
        ipAddress = "127.0.0.1"; // Convert IPv6 to IPv4
    }
    vnpParamsMap.put("vnp_IpAddr", ipAddress);

    // ✅ FIX: Use vnp_OrderInfo instead of vnp_OrderNote
    if (orderNote != null && !orderNote.isEmpty()) {
        vnpParamsMap.put("vnp_OrderInfo", orderNote);
    }
    
    // Debug logging
    System.out.println("=== VNPay Payment Parameters ===");
    vnpParamsMap.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> System.out.println(entry.getKey() + " = " + entry.getValue()));
    
    // Build query url
    String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
    String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
    
    System.out.println("Hash Data: " + hashData);
    
    String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
    System.out.println("Signature: " + vnpSecureHash);
    System.out.println("=== End VNPay Parameters ===\n");
    
    queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
    String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    
    return PaymentDTO.VNPayResponse.builder()
            .code("ok")
            .message("success")
            .paymentUrl(paymentUrl)
            .build();
}
```

### File: `VNPayUtil.java`

```java
public static String getIpAddress(HttpServletRequest request) {
    if (request == null) {
        return "127.0.0.1";
    }
    
    String ipAddress;
    try {
        ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = "127.0.0.1";
        }
        
        // Convert IPv6 localhost to IPv4
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        
        // If still IPv6 format, convert to IPv4
        if (ipAddress.contains(":")) {
            if (ipAddress.contains(".")) {
                // IPv4-mapped IPv6
                ipAddress = ipAddress.substring(ipAddress.lastIndexOf(":") + 1);
            } else {
                // Pure IPv6
                ipAddress = "127.0.0.1";
            }
        }
    } catch (Exception e) {
        ipAddress = "127.0.0.1";
    }
    return ipAddress;
}
```

## 📋 VNPay Valid Parameters

Theo tài liệu VNPay, các parameters hợp lệ:

### Required Parameters:
- ✅ `vnp_Version` - Phiên bản API (2.1.0)
- ✅ `vnp_Command` - Mã lệnh (pay)
- ✅ `vnp_TmnCode` - Mã website
- ✅ `vnp_Amount` - Số tiền (VND * 100)
- ✅ `vnp_CurrCode` - Đơn vị tiền tệ (VND)
- ✅ `vnp_TxnRef` - Mã giao dịch
- ✅ `vnp_OrderInfo` - Thông tin đơn hàng ⚠️
- ✅ `vnp_OrderType` - Loại đơn hàng
- ✅ `vnp_Locale` - Ngôn ngữ (vn/en)
- ✅ `vnp_ReturnUrl` - URL callback
- ✅ `vnp_IpAddr` - IP khách hàng (IPv4 only!)
- ✅ `vnp_CreateDate` - Thời gian tạo
- ✅ `vnp_ExpireDate` - Thời gian hết hạn
- ✅ `vnp_SecureHash` - Chữ ký

### Optional Parameters:
- ✅ `vnp_BankCode` - Mã ngân hàng (nếu muốn chọn sẵn)
- ✅ `vnp_Bill_*` - Thông tin hóa đơn
- ✅ `vnp_Inv_*` - Thông tin xuất hóa đơn

### ❌ Invalid Parameters:
- ❌ `vnp_OrderNote` - KHÔNG tồn tại!
- ❌ Any parameter with IPv6 value

## 🧪 Test

### Request Body:
```json
{
    "amount": 100000,
    "bankCode": "",
    "note": "Test payment",
    "returnUrl": "http://localhost:5173/payment"
}
```

### Expected Console Output:
```
=== VNPay Payment Parameters ===
vnp_Amount = 10000000
vnp_Command = pay
vnp_CreateDate = 20251007231436
vnp_CurrCode = VND
vnp_ExpireDate = 20251007232936
vnp_IpAddr = 127.0.0.1  ✅ IPv4
vnp_Locale = vn
vnp_OrderInfo = Test payment  ✅ NOT vnp_OrderNote
vnp_OrderType = other
vnp_ReturnUrl = http://localhost:5173/payment
vnp_TmnCode = 1FO325BY
vnp_TxnRef = 12345678
vnp_Version = 2.1.0
Hash Data: vnp_Amount=10000000&...
Signature: [128 chars]
=== End VNPay Parameters ===
```

### Expected Result:
- ✅ VNPay page loads successfully
- ✅ Shows bank selection page (if no bankCode)
- ✅ Shows correct amount
- ✅ No "Ngân hàng không được hỗ trợ" error

## 🔑 Key Takeaways

1. **Luôn dùng `vnp_OrderInfo`**, KHÔNG phải `vnp_OrderNote`
2. **Luôn convert IPv6 → IPv4** trước khi gửi VNPay
3. **Check console logs** để verify parameters
4. **Lỗi "Ngân hàng không được hỗ trợ"** thường do parameter sai, không phải ngân hàng thực sự

## 📚 References

- [VNPay API Documentation](https://sandbox.vnpayment.vn/apis/docs/huong-dan-tich-hop/)
- [VNPay Parameter List](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html#tao-url-thanh-toan)

