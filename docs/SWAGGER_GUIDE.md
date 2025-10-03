# 📚 Swagger UI Guide - Pet Care Backend

## 🌐 Truy cập Swagger UI

### **Development Environment:**
```
http://localhost:8081/swagger-ui.html
```

### **API Documentation JSON:**
```
http://localhost:8081/v3/api-docs
```

## 🔓 Authentication Status

### **✅ Authentication DISABLED for Testing**
- **Không cần JWT token** để test APIs trong Swagger
- **Tất cả endpoints** đều có thể test trực tiếp
- **Không cần login** trước khi sử dụng
- **Ideal for development** và API exploration

### **🔐 Production Note:**
Trong production environment, authentication sẽ được bật lại để bảo mật APIs.

## 🎯 Cách sử dụng Swagger UI

### **Bước 1: Khởi động ứng dụng**
```bash
mvn spring-boot:run
```

### **Bước 2: Mở Swagger UI**
Truy cập: `http://localhost:8081/swagger-ui.html`

### **Bước 3: Explore APIs**
1. **Browse by Tags** - APIs được nhóm theo chức năng:
   - 🔐 Authentication
   - 👥 Customer Management  
   - 🐾 Pet Management
   - 📅 Appointment Management
   - 🏥 Medical Management
   - 📚 Swagger Example

2. **Click vào endpoint** muốn test
3. **Click "Try it out"**
4. **Nhập parameters** (nếu có)
5. **Click "Execute"**
6. **Xem response** ngay lập tức

## 📋 API Groups Available

### **🔐 Authentication**
- `POST /auth/login` - User login (vẫn hoạt động để test JWT)

### **👥 Customer Management**
- `POST /api/user/v1/customers` - Create customer
- `GET /api/user/v1/customers` - Get all customers
- `GET /api/user/v1/customers/{id}` - Get customer by ID
- `PUT /api/user/v1/customers/{id}` - Update customer
- `DELETE /api/user/v1/customers/{id}` - Delete customer

### **🐾 Pet Management**
- `POST /api/user/v1/pets` - Register new pet
- `GET /api/user/v1/pets` - Get all pets
- `GET /api/user/v1/pets/{id}` - Get pet details
- `PUT /api/user/v1/pets/{id}` - Update pet info
- `POST /api/user/v1/pets/{id}/weight` - Record pet weight

### **📅 Appointment Management**
- `POST /api/user/v1/appointments` - Book appointment
- `GET /api/user/v1/appointments` - List appointments
- `PUT /api/user/v1/appointments/{id}` - Update appointment
- `DELETE /api/user/v1/appointments/{id}` - Cancel appointment

### **🏥 Medical Management**
- `POST /api/user/v1/visits` - Create visit record
- `POST /api/user/v1/diagnoses` - Add diagnosis
- `POST /api/user/v1/test-results` - Record test results
- `POST /api/user/v1/prescriptions` - Create prescription

### **📚 Example Endpoints**
- `GET /api/user/v1/swagger-example/system-info` - System information
- `GET /api/user/v1/swagger-example/user-stats/{userId}` - User statistics
- `GET /api/user/v1/swagger-example/search` - Search with filters
- `POST /api/user/v1/swagger-example/create` - Create example data

## 🧪 Testing Examples

### **Example 1: Create Customer**
```json
POST /api/user/v1/customers
{
  "fullname": "John Doe",
  "email": "john.doe@example.com",
  "phone": "0123456789",
  "gender": "MALE",
  "address": "123 Main Street, City",
  "dateOfBirth": "1990-01-15"
}
```

### **Example 2: Register Pet**
```json
POST /api/user/v1/pets
{
  "name": "Buddy",
  "breed": "Golden Retriever",
  "gender": "MALE",
  "dateOfBirth": "2020-03-15",
  "color": "Golden",
  "customerId": 1,
  "animalTypeId": 1
}
```

### **Example 3: Book Appointment**
```json
POST /api/user/v1/appointments
{
  "customerId": 1,
  "workScheduleId": 1,
  "appointmentDate": "2024-01-20",
  "appointmentTime": "10:00:00",
  "notes": "Regular checkup for Buddy"
}
```

## 🎨 Swagger UI Features

### **✨ Available Features:**
- **Interactive Testing** - Execute APIs directly
- **Request/Response Examples** - See sample data
- **Parameter Documentation** - Detailed parameter info
- **Schema Definitions** - Data model documentation
- **Error Code Documentation** - HTTP status meanings
- **Search Functionality** - Find APIs quickly
- **Collapsible Sections** - Organized view
- **Export Options** - Download API specs

### **🔍 Search Tips:**
- Use **search box** to find specific endpoints
- **Filter by tags** to focus on specific modules
- **Expand/Collapse** sections for better navigation

## 📊 Response Codes

### **Success Codes:**
- **200 OK** - Request successful
- **201 Created** - Resource created successfully

### **Client Error Codes:**
- **400 Bad Request** - Invalid input data
- **404 Not Found** - Resource not found
- **409 Conflict** - Resource already exists

### **Server Error Codes:**
- **500 Internal Server Error** - Server error

## 🛠️ Development Tips

### **For Developers:**
1. **Test APIs** before frontend integration
2. **Validate request/response** formats
3. **Check error handling** scenarios
4. **Explore data models** and relationships
5. **Generate client code** from OpenAPI spec

### **For QA Testing:**
1. **Functional testing** of all endpoints
2. **Boundary testing** with edge cases
3. **Error scenario testing**
4. **Data validation testing**
5. **Integration testing** workflows

## 🔄 Re-enabling Authentication

Để bật lại authentication trong production:

1. **Uncomment security config** trong `SwaggerConfig.java`
2. **Add @SecurityRequirement** annotations vào controllers
3. **Update API documentation** để reflect authentication requirements
4. **Test with JWT tokens** trong production environment

## 📞 Support

Nếu gặp vấn đề với Swagger UI:
- **Check console logs** cho errors
- **Verify application.properties** configuration
- **Restart application** nếu cần
- **Contact development team** để hỗ trợ

---

**Happy API Testing!** 🚀
