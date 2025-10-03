package org.example.petcarebe.service;

import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
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
