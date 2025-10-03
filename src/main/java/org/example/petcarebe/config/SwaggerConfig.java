package org.example.petcarebe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Development Server"),
                        new Server().url("https://api.petcare.com").description("Production Server")
                ));
                // Authentication disabled for easier API testing
    }

    private Info apiInfo() {
        return new Info()
                .title("🐾 Pet Care Backend API")
                .description("""
                        ## Pet Care Management System API Documentation
                        
                        Hệ thống quản lý phòng khám thú y toàn diện với các tính năng:
                        
                        ### 🚀 Tính năng chính:
                        - **👥 User Management**: Quản lý người dùng và phân quyền
                        - **🐕 Pet Management**: Quản lý thông tin thú cưng
                        - **📅 Appointment**: Đặt lịch và quản lý lịch hẹn
                        - **🏥 Medical Records**: Hồ sơ y tế và khám chữa bệnh
                        - **💊 Prescription**: Kê đơn thuốc và quản lý thuốc
                        - **💰 Invoice & Payment**: Hóa đơn và thanh toán
                        - **📊 Statistics**: Báo cáo và thống kê
                        
                        ### 🔐 Authentication:
                        **Authentication đã được tắt cho Swagger testing**
                        - Bạn có thể test tất cả API endpoints mà không cần JWT token
                        - Trong production, authentication sẽ được bật lại
                        - Để test authentication, sử dụng `/auth/login` endpoint
                        
                        ### 👤 User Roles:
                        - **ADMIN**: Toàn quyền quản lý hệ thống
                        - **DOCTOR**: Quản lý khám chữa bệnh, kê đơn
                        - **STAFF**: Quản lý lịch hẹn, hóa đơn
                        - **USER**: Khách hàng sử dụng dịch vụ
                        
                        ### 📱 Contact:
                        - **GitHub**: https://github.com/your-repo/petcare-backend
                        - **Email**: support@petcare.com
                        """)
                .version("v1.0.0")
                .contact(new Contact()
                        .name("Pet Care Development Team")
                        .email("dev@petcare.com")
                        .url("https://github.com/your-repo/petcare-backend"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    // Security scheme removed - authentication disabled for Swagger testing
}
