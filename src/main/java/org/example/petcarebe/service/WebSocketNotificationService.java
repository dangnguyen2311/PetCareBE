package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.notification.CreateNotificationBroadcastRequest;
import org.example.petcarebe.dto.request.notification.CreateNotificationRequest;
import org.example.petcarebe.dto.request.notification.CreateNotificationStaffOrDoctorRequest;
import org.example.petcarebe.dto.response.notification.BroadcastNotificationResponse;
import org.example.petcarebe.dto.response.notification.CreateNotificationStaffOrDoctorResponse;
import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.example.petcarebe.dto.response.notification.SocketNotificationResponse;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.NotificationRepository;
import org.example.petcarebe.repository.UserRepository;
import org.example.petcarebe.repository.VaccineScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VaccineScheduleRepository vaccineScheduleRepository;
    
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


    public NotificationResponse sendPrivateNotification(CreateNotificationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        VaccineSchedule vaccineSchedule = null;
        if (request.getVaccineScheduleId() != null) {
            Optional<VaccineSchedule> vaccineScheduleOptional = vaccineScheduleRepository.findById(request.getVaccineScheduleId());
            vaccineSchedule = vaccineScheduleOptional.orElse(null);
        }
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setCreatedDate(LocalDate.now());
        notification.setStatus("GENERAL");
        notification.setCustomer(customer);
        notification.setVaccineSchedule(vaccineSchedule);
        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setId(savedNotification.getId());
        notificationResponse.setTitle(savedNotification.getTitle());
        notificationResponse.setMessage(savedNotification.getMessage());
        notificationResponse.setType(savedNotification.getType());
        notificationResponse.setCreatedAt(LocalDateTime.now());
        notificationResponse.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        notificationResponse.setIsRead(false);
        notificationResponse.setStatus("GENERAL");
        notificationResponse.setResponseMessage("Response: " + savedNotification.getMessage());
        notificationResponse.setCreatedDate(savedNotification.getCreatedDate());
        notificationResponse.setVaccineScheduleId(request.getVaccineScheduleId());
        notificationResponse.setCustomerId(customer != null ? customer.getId() : null);
        notificationResponse.setCustomerName(customer != null ? customer.getFullname() : null);
        notificationResponse.setCustomerEmail(customer != null ? customer.getEmail() : null);


        sendNotificationToUser(request.getCustomerId(), notificationResponse);
        return notificationResponse;
    }

    public CreateNotificationStaffOrDoctorResponse sendPrivateToSTaffOrDoctorNotification(CreateNotificationStaffOrDoctorRequest request) {
        String username = request.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setCreatedDate(LocalDate.now());
        notification.setStatus("GENERAL");

        Notification savedNotification = notificationRepository.save(notification);
        return convertToCreateNotificationStaffOrDoctorResponse(notification);

    }

    private CreateNotificationStaffOrDoctorResponse convertToCreateNotificationStaffOrDoctorResponse(Notification  notification) {
        return CreateNotificationStaffOrDoctorResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .createdDate(notification.getCreatedDate())
                .responseMessage(notification.getMessage())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .userId(0L)
                .timeStamp("")
                .build();
    }

}
