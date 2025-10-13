package org.example.petcarebe.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.petcarebe.dto.request.email.EmailTestHTMLRequest;
import org.example.petcarebe.dto.response.email.EmailTestHTMLResponse;
import org.example.petcarebe.dto.response.email.EmailTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String myEmail;

    private final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Gửi email xác thực HTML
     */
    public void sendVerificationEmail(String toEmail, String username, String token) throws MessagingException {
        String subject = "Xác thực tài khoản của bạn";
        String verificationLink = "http://localhost:8081/api/auth/verify?token=" + token;

        String htmlContent = "<h2>Xin chào " + username + ",</h2>"
                + "<p>Cảm ơn bạn đã đăng ký! Vui lòng nhấn vào link bên dưới để xác thực tài khoản:</p>"
                + "<a href=\"" + verificationLink + "\">Xác thực ngay</a>"
                + "<p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true để dùng HTML
        helper.setFrom("your_email@gmail.com");

        mailSender.send(message);
    }

    public void sendAppointmentPendingEmail(String to, Map<String, Object> data) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()
        );
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process("appointment-pending.html", context);

        helper.setTo(to);
        helper.setSubject("Thông báo hẹn lịch khám thú cưng của bạn");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendAppointmentConfirmationEmail(String to, Map<String, Object> appointmentData) throws MessagingException {
        // 1. Tạo đối tượng email
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        // 2. Chuẩn bị dữ liệu để đổ vào template
        Context context = new Context();
        context.setVariables(appointmentData);

        // 3. Render template HTML
        String htmlContent = templateEngine.process("appointment-confirmation.html", context);

        // 4. Cấu hình email
        helper.setTo(to);
        helper.setSubject("Xác nhận lịch hẹn khám thú cưng");
        helper.setText(htmlContent, true);

        // 5. Gửi
        mailSender.send(message);
    }

    public EmailTestResponse sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        mailSender.send(simpleMailMessage);
        return convertToEmailTestResponse(simpleMailMessage, "Email sent successfully");
    }

    public EmailTestHTMLResponse sendTestHtmlEmail(EmailTestHTMLRequest request) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(request.getHtmlContent(), true); // true => nội dung HTML
        helper.setFrom(myEmail);
        mailSender.send(mimeMessage);
        return convertToEmailTestHTMLResponse(mimeMessage);
    }

    private EmailTestResponse convertToEmailTestResponse(SimpleMailMessage simpleMailMessage) {
        return EmailTestResponse.builder()
                .subject(simpleMailMessage.getSubject())
                .message("")
                .to(simpleMailMessage.getTo())
                .success(true)
                .build();
    }

    private EmailTestResponse convertToEmailTestResponse(SimpleMailMessage simpleMailMessage, String message) {
        return EmailTestResponse.builder()
                .subject(simpleMailMessage.getSubject())
                .message(message)
                .to(simpleMailMessage.getTo())
                .success(true)
                .build();
    }

    private EmailTestHTMLResponse convertToEmailTestHTMLResponse(MimeMessage mimeMessage) throws MessagingException, IOException {
        return EmailTestHTMLResponse.builder()
                .message(mimeMessage.getContent().toString())
                .success(true)
                .build();
    }

}