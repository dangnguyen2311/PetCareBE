package org.example.petcarebe.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.notification.CreateNotificationBroadcastRequest;
import org.example.petcarebe.dto.request.notification.CreateNotificationRequest;
import org.example.petcarebe.dto.request.notification.UpdateNotificationStatusRequest;
import org.example.petcarebe.dto.response.notification.BroadcastNotificationResponse;
import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.example.petcarebe.model.Notification;
import org.example.petcarebe.service.NotificationService;
import org.example.petcarebe.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/v1/notifications")
@Tag(name = "🔔 Notification Management", description = "Endpoints for managing notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Operation(
            summary = "Create new notification",
            description = "Create a new notification for a customer"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Customer or vaccine schedule not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        try {
            NotificationResponse response = notificationService.createNotification(request);
            // Send real-time notification via WebSocket
            webSocketNotificationService.sendNotificationToUser(request.getCustomerId(), response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/notification/{username}")
    public ResponseEntity<?> sendTestNotification(@PathVariable String username) {
        NotificationResponse notification = new NotificationResponse();
        notification.setId(System.currentTimeMillis());
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification sent at " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        notification.setType("info");

        webSocketNotificationService.sendNotificationToUserName(username, notification);

        return ResponseEntity.ok("Private Notification sent to " + username);
    }

    @Operation(
            summary = "Send broadcast notification",
            description = "Send a notification to all connected users via WebSocket"
    )
    @PostMapping("/broadcast")
    public ResponseEntity<BroadcastNotificationResponse> broadcastNotification(
            @RequestBody CreateNotificationBroadcastRequest request
            ) {
        try {
            BroadcastNotificationResponse response = webSocketNotificationService.sendBroadcastNotification(request);
//            return ResponseEntity.ok(Map.of("message", "Broadcast notification sent successfully"));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e) {
            BroadcastNotificationResponse error = new BroadcastNotificationResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            BroadcastNotificationResponse error = new BroadcastNotificationResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/broadcast-2")
    public ResponseEntity<?> sendBroadcastNotification() {
        webSocketNotificationService.notifySystemMaintenance(
                "Hệ thống sẽ bảo trì từ 22:00 - 23:00 hôm nay. Vui lòng lưu công việc."
        );

        return ResponseEntity.ok("Broadcast notification sent");
    }

    @Operation(
            summary = "Get notification by ID",
            description = "Retrieve a specific notification by its ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(
            @Parameter(description = "ID of the notification", example = "1", required = true)
            @PathVariable Long id) {
        try {
            NotificationResponse notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get notifications by customer",
            description = "Retrieve all notifications for a specific customer"
    )
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByCustomer(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByCustomerId(customerId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Get notifications by customer with pagination",
            description = "Retrieve notifications for a customer with pagination"
    )
    @GetMapping("/customer/{customerId}/paginated")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByCustomerPaginated(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = notificationService.getNotificationsByCustomerId(customerId, page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Get unread notifications",
            description = "Retrieve all unread notifications for a customer"
    )
    @GetMapping("/customer/{customerId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId) {
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(customerId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Get unread notification count",
            description = "Get the count of unread notifications for a customer"
    )
    @GetMapping("/customer/{customerId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationCount(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId) {
        Long count = notificationService.getUnreadNotificationCount(customerId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @Operation(
            summary = "Update notification status",
            description = "Update the status of a notification (UNREAD, READ, ARCHIVED)"
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationResponse> updateNotificationStatus(
            @Parameter(description = "ID of the notification", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationStatusRequest request) {
        try {
            NotificationResponse updatedNotification = notificationService.updateNotificationStatus(id, request);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Mark notification as read",
            description = "Mark a specific notification as read"
    )
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID of the notification", example = "1", required = true)
            @PathVariable Long id) {
        try {
            NotificationResponse updatedNotification = notificationService.markAsRead(id);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Mark all notifications as read",
            description = "Mark all unread notifications as read for a customer"
    )
    @PutMapping("/customer/{customerId}/mark-all-read")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId) {
        try {
            notificationService.markAllAsReadForCustomer(customerId);
            return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Delete notification",
            description = "Delete a specific notification"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @Parameter(description = "ID of the notification", example = "1", required = true)
            @PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get recent notifications",
            description = "Get recent notifications for a customer (limited number)"
    )
    @GetMapping("/customer/{customerId}/recent")
    public ResponseEntity<List<NotificationResponse>> getRecentNotifications(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId,
            @Parameter(description = "Number of notifications to retrieve", example = "5")
            @RequestParam(defaultValue = "5") int limit) {
        List<NotificationResponse> notifications = notificationService.getRecentNotifications(customerId, limit);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Get notifications by type",
            description = "Retrieve notifications by type for a customer"
    )
    @GetMapping("/customer/{customerId}/type/{type}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByType(
            @Parameter(description = "ID of the customer", example = "1", required = true)
            @PathVariable Long customerId,
            @Parameter(description = "Type of notification", example = "APPOINTMENT", required = true)
            @PathVariable String type) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByCustomerAndType(customerId, type);
        return ResponseEntity.ok(notifications);
    }

    // ==================== ADMIN ENDPOINTS ====================

    @Operation(
            summary = "[ADMIN] Get all notifications",
            description = "Admin: Retrieve all notifications in the system"
    )
    @GetMapping("/admin/all")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "[ADMIN] Get notifications by type",
            description = "Admin: Retrieve all notifications of a specific type"
    )
    @GetMapping("/admin/type/{type}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByTypeAdmin(
            @Parameter(description = "Type of notification", example = "APPOINTMENT", required = true)
            @PathVariable String type) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    // ==================== WEBSOCKET ENDPOINTS ====================

    @Operation(
            summary = "Send real-time notification to user",
            description = "Send a real-time notification to a specific user via WebSocket"
    )
    @PostMapping("/send-realtime")
    public ResponseEntity<Map<String, String>> sendRealtimeNotification(
            @Parameter(description = "User ID to send notification to", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Notification title", required = true)
            @RequestParam String title,
            @Parameter(description = "Notification message", required = true)
            @RequestParam String message,
            @Parameter(description = "Notification type", example = "INFO")
            @RequestParam(defaultValue = "INFO") String type) {
        try {
            NotificationResponse notification = NotificationResponse.builder()
                    .title(title)
                    .message(message)
                    .type(type)
                    .userId(userId)
                    .isRead(false)
                    .responseMessage("Real-time notification sent successfully")
                    .build();

            webSocketNotificationService.sendNotificationToUser(userId, notification);
            return ResponseEntity.ok(Map.of("message", "Real-time notification sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



    @Operation(
            summary = "Send appointment reminder",
            description = "Send appointment reminder notification via WebSocket"
    )
    @PostMapping("/send-appointment-reminder")
    public ResponseEntity<Map<String, String>> sendAppointmentReminder(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Pet name", required = true)
            @RequestParam String petName,
            @Parameter(description = "Appointment time", required = true)
            @RequestParam String appointmentTime) {
        try {
            webSocketNotificationService.sendAppointmentReminder(userId, petName, appointmentTime);
            return ResponseEntity.ok(Map.of("message", "Appointment reminder sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Send vaccine reminder",
            description = "Send vaccine schedule reminder notification via WebSocket"
    )
    @PostMapping("/send-vaccine-reminder")
    public ResponseEntity<Map<String, String>> sendVaccineReminder(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Pet name", required = true)
            @RequestParam String petName,
            @Parameter(description = "Vaccine name", required = true)
            @RequestParam String vaccineName,
            @Parameter(description = "Schedule date", required = true)
            @RequestParam String scheduleDate) {
        try {
            webSocketNotificationService.sendVaccineReminder(userId, petName, vaccineName, scheduleDate);
            return ResponseEntity.ok(Map.of("message", "Vaccine reminder sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Send payment notification",
            description = "Send payment status notification via WebSocket"
    )
    @PostMapping("/send-payment-notification")
    public ResponseEntity<Map<String, String>> sendPaymentNotification(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "Payment status", required = true)
            @RequestParam String status,
            @Parameter(description = "Payment amount", required = true)
            @RequestParam Double amount) {
        try {
            webSocketNotificationService.sendPaymentNotification(userId, status, amount);
            return ResponseEntity.ok(Map.of("message", "Payment notification sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Send notification to role",
            description = "Send notification to all users with specific role via WebSocket"
    )
    @PostMapping("/send-to-role")
    public ResponseEntity<Map<String, String>> sendNotificationToRole(
            @Parameter(description = "Role name", required = true, example = "ADMIN")
            @RequestParam String role,
            @Parameter(description = "Notification title", required = true)
            @RequestParam String title,
            @Parameter(description = "Notification message", required = true)
            @RequestParam String message,
            @Parameter(description = "Notification type", example = "INFO")
            @RequestParam(defaultValue = "INFO") String type) {
        try {
            NotificationResponse notification = NotificationResponse.builder()
                    .title(title)
                    .message(message)
                    .type(type)
                    .isRead(false)
                    .responseMessage("Role-based notification sent successfully")
                    .build();

            webSocketNotificationService.sendNotificationToRole(role, notification);
            return ResponseEntity.ok(Map.of("message", "Notification sent to " + role + " role successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Send system notification to admins",
            description = "Send system notification to all admin users via WebSocket"
    )
    @PostMapping("/send-system-notification")
    public ResponseEntity<Map<String, String>> sendSystemNotification(
            @Parameter(description = "Notification title", required = true)
            @RequestParam String title,
            @Parameter(description = "Notification message", required = true)
            @RequestParam String message) {
        try {
            webSocketNotificationService.sendSystemNotificationToAdmins(title, message);
            return ResponseEntity.ok(Map.of("message", "System notification sent to admins successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")    // server gửi lại cho tất cả client đang sub
    public String sendMessage(String message) {
        return "Server received: " + message;
    }
}

