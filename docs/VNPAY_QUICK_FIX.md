# 🚀 VNPay Quick Fix - Lỗi Sai Chữ Ký

## ✅ Đã Fix

### 1. **Encoding Logic**
- ❌ Cũ: Encode cả key và value
- ✅ Mới: Chỉ encode value, không encode key

### 2. **Signature Verification**
- ❌ Cũ: Không verify signature khi callback
- ✅ Mới: Verify signature trước khi xử lý payment

### 3. **HttpServletRequest Missing**
- ❌ Cũ: Pass `null` vào `createPaymentUrl()` → IP address lỗi
- ✅ Mới: Pass `httpRequest` từ controller → service → vnpayService

### 4. **Invoice Status Update**
- ✅ Tự động update invoice status = PAID khi payment success

### 5. **Null Safety**
- ✅ Handle null request trong `getIpAddress()` → default "127.0.0.1"

### 6. **IPv6 to IPv4 Conversion** ⚠️ **QUAN TRỌNG!**
- ❌ Cũ: IP = `0:0:0:0:0:0:0:1` (IPv6) → VNPay reject
- ✅ Mới: Convert IPv6 → IPv4 `127.0.0.1`
- **VNPay chỉ chấp nhận IPv4 format!**

## 🧪 Cách Test

### Option 1: Test qua API endpoint
```bash
# Test signature generation
GET http://localhost:8081/api/user/v1/payments/test-vnpay-signature
```

### Option 2: Chạy debug tool
```bash
# Compile và chạy
cd src/main/java
javac org/example/petcarebe/util/VnpayDebugUtil.java
java org.example.petcarebe.util.VnpayDebugUtil
```

### Option 3: Test thực tế
1. Tạo payment qua API
2. Click vào payment URL
3. Thanh toán trên VNPay sandbox
4. Kiểm tra logs console
5. Verify payment status

## 📝 Files đã thay đổi

1. ✅ `VnpayUtil.java` - Fix encoding + thêm verify method + null safety
2. ✅ `PaymentService.java` - Thêm signature verification + pass HttpServletRequest
3. ✅ `PaymentController.java` - Pass httpRequest + secretKey + logging
4. ✅ `VnpayService.java` - Thêm getSecretKey() + logging debug
5. ✅ `VnpayUtilTest.java` - Unit tests
6. ✅ `VnpayDebugUtil.java` - Debug tool
7. ✅ `docs/VNPAY_FIX_GUIDE.md` - Chi tiết hướng dẫn

## 🔧 Chi tiết thay đổi

### PaymentController.java
```java
// Trước
paymentService.createPayment(request);

// Sau
paymentService.createPayment(request, httpRequest); // Pass httpRequest
```

### PaymentService.java
```java
// Trước
public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
    vnpayService.createPaymentUrl(convertToVnpayRequest(request, transactionCode), null);
}

// Sau
public CreatePaymentResponse createPayment(CreatePaymentRequest request, HttpServletRequest httpRequest) {
    vnpayService.createPaymentUrl(convertToVnpayRequest(request, transactionCode), httpRequest);
}
```

### VnpayUtil.java
```java
// Thêm null check + IPv6 to IPv4 conversion
public static String getIpAddress(HttpServletRequest request) {
    if (request == null) {
        return "127.0.0.1"; // Default IP
    }

    String ipAddress = request.getRemoteAddr();

    // Convert IPv6 localhost to IPv4
    if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
        ipAddress = "127.0.0.1";
    }

    // If still IPv6, use default IPv4
    if (ipAddress.contains(":")) {
        ipAddress = "127.0.0.1";
    }

    return ipAddress;
}
```

## 🔑 Key Points

1. **Hash Data**: KHÔNG encode gì cả
2. **Query URL**: CHỈ encode value
3. **Verify**: Luôn verify signature từ VNPay
4. **Secret Key**: Phải khớp với TMN Code
5. **IP Address**: CHỈ dùng IPv4, KHÔNG dùng IPv6 ⚠️

## 🎯 Next Steps

1. Restart server
2. Test endpoint: `/api/user/v1/payments/test-vnpay-signature`
3. Thử thanh toán thực tế
4. Kiểm tra logs khi callback

## 📞 Debug

Nếu vẫn lỗi, check logs:
```
=== VNPay Callback Parameters ===
vnp_Amount = ...
vnp_SecureHash = ...
Secret Key: X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA
```

So sánh signature từ VNPay với signature tự tính.

