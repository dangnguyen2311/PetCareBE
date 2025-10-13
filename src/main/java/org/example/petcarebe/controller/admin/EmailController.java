package org.example.petcarebe.controller.admin;

import jakarta.mail.MessagingException;
import org.example.petcarebe.dto.request.email.EmailTestHTMLRequest;
import org.example.petcarebe.dto.request.email.EmailTestRequest;
import org.example.petcarebe.dto.response.email.EmailTestHTMLResponse;
import org.example.petcarebe.dto.response.email.EmailTestResponse;
import org.example.petcarebe.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // API giả lập đăng ký -> gửi email xác thực
    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String username) throws MessagingException {
        // Giả lập tạo token
        String token = UUID.randomUUID().toString();
        // Gửi email
        emailService.sendVerificationEmail(email, username, token);
        return "Đã gửi email xác thực tới " + email;
    }

    // API xác thực
    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        // Xử lý xác thực token ở đây
        return "Tài khoản đã được xác thực thành công với token: " + token;
    }

    @PostMapping("/test")
    public ResponseEntity<EmailTestResponse> sendEmail(@RequestBody EmailTestRequest request) {
        try {
            EmailTestResponse response = emailService.sendSimpleEmail(request.getTo(), request.getSubject(), request.getBody());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            EmailTestResponse errorResponse = new EmailTestResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        } catch (Exception e) {
            EmailTestResponse errorResponse = new EmailTestResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/test-html")
    public ResponseEntity<EmailTestHTMLResponse> sendEmailHtml(@RequestBody EmailTestHTMLRequest request) {
        try{
            EmailTestHTMLResponse response = emailService.sendTestHtmlEmail(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            EmailTestHTMLResponse errorResponse = new EmailTestHTMLResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        } catch (Exception e) {
            EmailTestHTMLResponse errorResponse = new EmailTestHTMLResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}