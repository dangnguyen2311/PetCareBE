package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.notification.CreateNotificationBroadcastRequest;
import org.example.petcarebe.dto.response.notification.BroadcastNotificationResponse;
import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.example.petcarebe.dto.response.notification.SocketNotificationResponse;
import org.example.petcarebe.model.Notification;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.NotificationRepository;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    
    /**
     * Send notification to specific user
     */
    public void sendNotificationToUser(Long userId, NotificationResponse notification) {
        messagingTemplate.convertAndSendToUser(
            userId.toString(), 
            "/queue/notifications", 
            notification
        );
    }

    public void sendNotificationToUser(String username, NotificationResponse notification) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }
    /**
     * Send broadcast
     */
    public void sendBroadcastNotification(SocketNotificationResponse notification) {
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
    }
    /**
     * Send broadcast
     */
    @Transactional
    public BroadcastNotificationResponse sendBroadcastNotification(CreateNotificationBroadcastRequest request) {
        BroadcastNotificationResponse notification = new BroadcastNotificationResponse();
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus("GENERAL");
        notification.setIsRead(false);
        notification.setResponseMessage("Response: " + request.getMessage());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
        Notification notificationToSave = new Notification();
        notificationToSave.setTitle(request.getTitle());
        notificationToSave.setMessage(request.getMessage());
        notificationToSave.setType(request.getType());
        notificationToSave.setCreatedDate(LocalDate.now());
        notificationToSave.setStatus("GENERAL");

        Notification savedNotification = notificationRepository.save(notificationToSave);
        return notification;
    }

    /**
     * Send notification to specific user
     */
    public void sendNotificationToUserName(String username, NotificationResponse notification) {
        System.out.println("sendNotificationToUserName: username = "+username);
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }

    /**
     * Thông báo hệ thống
     */
    public void notifySystemMaintenance(String message) {
        SocketNotificationResponse notification = new SocketNotificationResponse();
        notification.setTitle("Thông báo hệ thống");
        notification.setMessage(message);
        notification.setType("warning");
        notification.setTimestamp(Instant.now().toString());

        sendBroadcastNotification(notification);
    }
    
    /**
     * Send notification to all users
     */
    public void sendNotificationToAll(NotificationResponse notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    
    /**
     * Send notification by role
     */
    public void sendNotificationToRole(String role, NotificationResponse notification) {
        messagingTemplate.convertAndSend("/topic/notifications/" + role.toLowerCase(), notification);
    }
    
    /**
     * Send appointment reminder
     */
    public void sendAppointmentReminder(Long userId, String petName, String appointmentTime) {
        NotificationResponse notification = NotificationResponse.builder()
                .title("Appointment Reminder")
                .message("Reminder: " + petName + " has an appointment at " + appointmentTime)
                .type("INFO")
//                .category("APPOINTMENT")
//                .userId(userId)
//                .isRead(false)
                .build();
                
        sendNotificationToUser(userId, notification);
    }
    
    /**
     * Send vaccine schedule reminder
     */
    public void sendVaccineReminder(Long userId, String petName, String vaccineName, String scheduleDate) {
        NotificationResponse notification = NotificationResponse.builder()
                .title("Vaccine Reminder")
                .message(petName + " is scheduled for " + vaccineName + " vaccination on " + scheduleDate)
                .type("WARNING")
//                .category("VACCINE")
//                .userId(userId)
//                .isRead(false)
                .build();
                
        sendNotificationToUser(userId, notification);
    }
    
    /**
     * Send payment notification
     */
    public void sendPaymentNotification(Long userId, String status, Double amount) {
        NotificationResponse notification = NotificationResponse.builder()
                .title("Payment " + status)
                .message("Payment of $" + amount + " has been " + status.toLowerCase())
                .type("SUCCESS".equals(status) ? "SUCCESS" : "ERROR")
//                .category("PAYMENT")
//                .userId(userId)
//                .isRead(false)
                .build();
                
        sendNotificationToUser(userId, notification);
    }
    
    /**
     * Send system notification to admins
     */
    public void sendSystemNotificationToAdmins(String title, String message) {
        NotificationResponse notification = NotificationResponse.builder()
                .title(title)
                .message(message)
                .type("INFO")
//                .category("SYSTEM")
//                .isRead(false)
                .build();
                
        sendNotificationToRole("ADMIN", notification);
    }




}
