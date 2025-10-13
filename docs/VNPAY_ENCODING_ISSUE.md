# 🔐 VNPay Encoding Issue - Critical Fix

## 🐛 Vấn đề

Mặc dù đã fix `vnp_OrderInfo` và IPv6, vẫn gặp lỗi **"Ngân hàng thanh toán không được hỗ trợ"**.

## 🔍 Root Cause

### Code cũ (SAI):
```java
public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
    return paramsMap.entrySet().stream()
        .map(entry ->
            (encodeKey ? URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII)
                : entry.getKey()) + "=" +
            URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))  // ❌ LUÔN encode value!
        .collect(Collectors.joining("&"));
}
```

**Vấn đề:**
- Parameter `encodeKey` chỉ control việc encode **key**
- **Value LUÔN được encode** (dòng cuối)
- Khi gọi `getPaymentURL(params, false)` để tạo hash data:
  - Key: KHÔNG encode ✅
  - Value: VẪN encode ❌ **SAI!**

### Ví dụ cụ thể:

**Input:**
```java
params.put("vnp_OrderInfo", "Thanh toan don hang #HD12345");
```

**Hash Data (encodeKey=false) - Code cũ:**
```
vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345  ❌ Value bị encode!
```

**Hash Data (encodeKey=false) - Đúng:**
```
vnp_OrderInfo=Thanh toan don hang #HD12345  ✅ Value KHÔNG encode!
```

**Query URL (encodeKey=true) - Đúng:**
```
vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345  ✅ Value encode!
```

### Tại sao lỗi?

1. **Tạo signature:**
   ```java
   String hashData = getPaymentURL(params, false);
   // hashData = "vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345"  ❌ Encoded
   String signature = hmacSHA512(secretKey, hashData);
   // signature = "abc123..."  ❌ Signature từ encoded data
   ```

2. **VNPay nhận được:**
   ```
   vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&vnp_SecureHash=abc123...
   ```

3. **VNPay verify:**
   ```java
   // VNPay decode URL first
   vnp_OrderInfo = "Thanh toan don hang #HD12345"  // Decoded
   
   // VNPay tạo hash data (KHÔNG encode)
   hashData = "vnp_OrderInfo=Thanh toan don hang #HD12345"  // NOT encoded
   
   // VNPay tính signature
   theirSignature = hmacSHA512(secretKey, hashData);
   // theirSignature = "xyz789..."  ✅ Signature từ unencoded data
   
   // Compare
   "abc123..." != "xyz789..."  ❌ KHÔNG KHỚP!
   ```

## ✅ Giải pháp

### Fix `VNPayUtil.getPaymentURL()`:

```java
/**
 * Build payment URL from parameters
 * @param paramsMap Parameters map
 * @param encodeValue true = encode values (for query URL), false = no encoding (for hash data)
 * @return URL string
 */
public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeValue) {
    return paramsMap.entrySet().stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> {
                String key = entry.getKey(); // NEVER encode key
                String value = encodeValue ? 
                    URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8) : 
                    entry.getValue();
                return key + "=" + value;
            })
            .collect(Collectors.joining("&"));
}
```

### Thay đổi:
1. ✅ Đổi parameter: `encodeKey` → `encodeValue` (rõ ràng hơn)
2. ✅ Key: **KHÔNG BAO GIỜ** encode
3. ✅ Value: 
   - `encodeValue=true` → Encode (cho query URL)
   - `encodeValue=false` → KHÔNG encode (cho hash data)
4. ✅ Charset: `US_ASCII` → `UTF_8` (chuẩn hơn)

## 📊 So sánh Before/After

### Test Data:
```java
Map<String, String> params = new HashMap<>();
params.put("vnp_Amount", "20000000");
params.put("vnp_OrderInfo", "Thanh toan don hang #HD12345");
params.put("vnp_TmnCode", "1FO325BY");
```

### Before Fix:

**Hash Data (encodeKey=false):**
```
vnp_Amount=20000000&vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&vnp_TmnCode=1FO325BY
```
❌ Value bị encode → Signature SAI!

**Query URL (encodeKey=true):**
```
vnp_Amount=20000000&vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&vnp_TmnCode=1FO325BY
```
✅ Đúng, nhưng signature đã sai từ trước

### After Fix:

**Hash Data (encodeValue=false):**
```
vnp_Amount=20000000&vnp_OrderInfo=Thanh toan don hang #HD12345&vnp_TmnCode=1FO325BY
```
✅ Value KHÔNG encode → Signature ĐÚNG!

**Query URL (encodeValue=true):**
```
vnp_Amount=20000000&vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&vnp_TmnCode=1FO325BY
```
✅ Value encode → URL hợp lệ

## 🧪 Test

### 1. Test Encoding Endpoint:
```bash
GET http://localhost:8081/api/user/v1/payments/test-encoding
```

**Expected Response:**
```json
{
    "hashData": "vnp_Amount=20000000&vnp_OrderInfo=Thanh toan don hang #HD12345&vnp_TmnCode=1FO325BY",
    "queryUrl": "vnp_Amount=20000000&vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&vnp_TmnCode=1FO325BY",
    "signature": "[128 chars]",
    "signatureLength": 128
}
```

**Check:**
- ✅ `hashData` KHÔNG có `+` hoặc `%23`
- ✅ `queryUrl` CÓ `+` và `%23`
- ✅ `signatureLength` = 128

### 2. Test Real Payment:
```bash
POST http://localhost:8081/api/user/v1/payments/vn-pay/1
{
    "amount": 200000,
    "bankCode": "NCB",
    "note": "Thanh toan don hang #HD12345",
    "returnUrl": "http://localhost:5173/payment"
}
```

**Check Console Logs:**
```
=== VNPay Payment Parameters ===
vnp_Amount = 20000000
vnp_OrderInfo = Thanh toan don hang #HD12345

--- Hash Data (NO encoding) ---
vnp_Amount=20000000&...&vnp_OrderInfo=Thanh toan don hang #HD12345&...
                                      ↑ NO encoding!

--- Query URL (WITH encoding) ---
vnp_Amount=20000000&...&vnp_OrderInfo=Thanh+toan+don+hang+%23HD12345&...
                                      ↑ WITH encoding!
```

**Expected Result:**
- ✅ VNPay page loads successfully
- ✅ No "Ngân hàng không được hỗ trợ" error
- ✅ Payment completes

## 🔑 Key Rules

### VNPay Signature Rules:

1. **Hash Data (for signature):**
   - Sort parameters alphabetically by key
   - Format: `key1=value1&key2=value2&...`
   - **NO encoding** for keys or values
   - Exclude `vnp_SecureHash` and `vnp_SecureHashType`

2. **Query URL (for request):**
   - Sort parameters alphabetically by key
   - Format: `key1=encodedValue1&key2=encodedValue2&...`
   - **NO encoding** for keys
   - **YES encoding** for values (UTF-8)
   - Append `&vnp_SecureHash=[signature]`

3. **Signature:**
   ```java
   String hashData = getPaymentURL(params, false);  // NO encoding
   String signature = hmacSHA512(secretKey, hashData);
   ```

4. **Full URL:**
   ```java
   String queryUrl = getPaymentURL(params, true);  // WITH encoding
   String fullUrl = payUrl + "?" + queryUrl + "&vnp_SecureHash=" + signature;
   ```

## 📝 Checklist

- [x] Fix `getPaymentURL()` method
- [x] Rename parameter: `encodeKey` → `encodeValue`
- [x] Key: NEVER encode
- [x] Value: Conditional encoding based on `encodeValue`
- [x] Change charset: `US_ASCII` → `UTF_8`
- [x] Add test endpoint
- [x] Add debug logging
- [x] Test with Vietnamese characters
- [x] Test with special characters (#, &, =, etc.)

## 🎯 Summary

**Root Cause:** Value luôn bị encode, kể cả khi tạo hash data.

**Fix:** Chỉ encode value khi `encodeValue=true` (query URL), không encode khi `encodeValue=false` (hash data).

**Result:** Signature khớp với VNPay → Payment thành công!

