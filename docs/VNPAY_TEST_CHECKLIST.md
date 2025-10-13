# ✅ VNPay Test Checklist

## 🔄 Restart Server
- [ ] Stop server
- [ ] Clean build: `mvn clean install`
- [ ] Start server: `mvn spring-boot:run`
- [ ] Verify server running on port 8081

## 🧪 Test 1: Signature Generation Test
**Endpoint:** `GET /api/user/v1/payments/test-vnpay-signature`

**Expected Result:**
```json
{
    "success": true,
    "isValid": true,
    "signatureLength": 128,
    "secretKey": "X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA"
}
```

- [ ] Test endpoint returns success
- [ ] `isValid` = true
- [ ] Signature length = 128 characters

## 🧪 Test 2: Create Payment
**Endpoint:** `POST /api/user/v1/payments`

**Request Body:**
```json
{
    "invoiceId": 1,
    "amount": 100000,
    "method": "VNPAY",
    "description": "Test payment for invoice 1"
}
```

**Check Console Logs:**
```
=== Creating VNPay Payment URL ===
Amount: 10000000
TxnRef: [8 digit number]
IP Address (converted): 127.0.0.1  ✅ MUST BE IPv4!
Hash Data: vnp_Amount=10000000&...&vnp_IpAddr=127.0.0.1&...
Signature: [128 character hash]
Secret Key: X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA
Full Payment URL: https://sandbox.vnpayment.vn/...
=== End Creating Payment URL ===
```

**⚠️ CRITICAL CHECK:**
- [ ] IP Address = `127.0.0.1` (IPv4)
- [ ] IP Address KHÔNG phải `0:0:0:0:0:0:0:1` (IPv6)
- [ ] Hash Data chứa `vnp_IpAddr=127.0.0.1`
- [ ] KHÔNG có warning "IP address still contains ':'"

**Expected Response:**
```json
{
    "success": true,
    "paymentId": 123,
    "transactionCode": "PAY_...",
    "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
    "message": "Payment created successfully"
}
```

- [ ] Response success = true
- [ ] paymentUrl is not null
- [ ] Console shows all debug logs
- [ ] IP Address is NOT "Invalid IP:..."

## 🧪 Test 3: VNPay Payment Flow
1. **Copy payment URL** from response
2. **Open in browser**
3. **Check URL parameters:**
   - [ ] vnp_Amount present
   - [ ] vnp_TxnRef present
   - [ ] vnp_SecureHash present (128 chars)
   - [ ] All parameters properly encoded

4. **On VNPay page:**
   - [ ] Page loads without "Sai chữ ký" error
   - [ ] Shows correct amount
   - [ ] Shows correct order info

5. **Complete payment:**
   - Use test card: `9704198526191432198`
   - Name: `NGUYEN VAN A`
   - Date: `07/15`
   - OTP: `123456`

6. **After payment:**
   - [ ] Redirects to return URL
   - [ ] Check console for callback logs:
   ```
   === VNPay Callback Parameters ===
   vnp_Amount = 10000000
   vnp_ResponseCode = 00
   vnp_SecureHash = [128 chars]
   ...
   Secret Key: X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA
   ```

## 🧪 Test 4: Verify Payment Status
**Endpoint:** `GET /api/user/v1/payments/{paymentId}`

**Expected:**
- [ ] Payment status = SUCCESS (if paid)
- [ ] Payment status = FAILED (if cancelled)
- [ ] Invoice status = PAID (if payment success)

## 🧪 Test 5: Callback Verification
**Manual test callback:**
```
GET /api/user/v1/payments/vnpay-callback?vnp_Amount=10000000&vnp_BankCode=NCB&vnp_ResponseCode=00&vnp_TxnRef=12345678&vnp_SecureHash=[valid_hash]
```

- [ ] Returns success if signature valid
- [ ] Returns error if signature invalid
- [ ] Payment status updated correctly

## ❌ Common Issues

### Issue 1: "Sai chữ ký" on VNPay page
**Check:**
- [ ] Secret key matches TMN Code
- [ ] Hash data is NOT encoded
- [ ] Query URL values ARE encoded
- [ ] All parameters sorted alphabetically

### Issue 2: IP Address = IPv6 format (0:0:0:0:0:0:0:1)
**Root Cause:** VNPay không chấp nhận IPv6!
**Check:**
- [ ] IP được convert sang IPv4 (127.0.0.1)
- [ ] Không có dấu `:` trong IP address
- [ ] Hash data chứa IPv4, không phải IPv6

### Issue 3: IP Address = "Invalid IP:..."
**Check:**
- [ ] HttpServletRequest is passed from controller
- [ ] Not passing null to createPaymentUrl()

### Issue 4: Callback fails
**Check:**
- [ ] Return URL is registered on VNPay
- [ ] Callback endpoint is accessible
- [ ] Signature verification is enabled

## 📊 Success Criteria

✅ All tests pass
✅ No "Sai chữ ký" error
✅ Payment completes successfully
✅ Payment status updates correctly
✅ Invoice status updates to PAID
✅ Console logs show correct data

## 🐛 If Still Failing

1. **Check logs** for exact error
2. **Compare signatures:**
   - Generated signature
   - VNPay's signature
3. **Verify config:**
   - TMN_CODE = 1FO325BY
   - SECRET_KEY = X5O7RBSH1JUDVBFMOSQ4EJ7UEGZQ9CBA
4. **Test with debug tool:**
   ```bash
   java org.example.petcarebe.util.VnpayDebugUtil
   ```
5. **Check VNPay sandbox status**

## 📞 Support

- VNPay Sandbox: https://sandbox.vnpayment.vn/
- VNPay Docs: https://sandbox.vnpayment.vn/apis/docs/
- Test Cards: https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html#danh-sach-ngan-hang

