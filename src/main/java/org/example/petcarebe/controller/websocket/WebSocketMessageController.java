package org.example.petcarebe.controller.websocket;

import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.example.petcarebe.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketMessageController {
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    /**
     * Handle user connection
     */
    @MessageMapping("/user.connect")
    @SendTo("/topic/public")
    public NotificationResponse connectUser(@Payload NotificationResponse notification,
                                          SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("userId", notification.getUserId());
        
        return NotificationResponse.builder()
                .title("User Connected")
                .message("User with id: " + notification.getUserId() + " connected")
                .type("INFO")
                .userId(notification.getUserId())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .responseMessage("Connection established successfully")
                .build();
    }
    
    /**
     * Handle user disconnect
     */
    @MessageMapping("/user.disconnect")
    @SendTo("/topic/public")
    public NotificationResponse disconnectUser(@Payload NotificationResponse notification,
                                             SimpMessageHeaderAccessor headerAccessor) {
        return NotificationResponse.builder()
                .title("User Disconnected")
                .message("User " + notification.getUserId() + " disconnected")
                .type("INFO")
                .userId(notification.getUserId())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .responseMessage("Disconnection processed successfully")
                .build();
    }
    
    /**
     * Handle private message
     */
    @MessageMapping("/private.message")
    public void sendPrivateMessage(@Payload NotificationResponse notification) {
        webSocketNotificationService.sendNotificationToUser(
            notification.getUserId(), 
            notification
        );
    }
    
    /**
     * Handle broadcast message
     */
    @MessageMapping("/public.message")
    @SendTo("/topic/notifications")
    public NotificationResponse sendPublicMessage(@Payload NotificationResponse notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setResponseMessage("Public message sent successfully");
        return notification;
    }
    
    /**
     * Handle typing indicator
     */
    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public NotificationResponse handleTyping(@Payload NotificationResponse notification) {
        return NotificationResponse.builder()
                .userId(notification.getUserId())
                .message("is typing...")
                .type("TYPING")
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
    }
}
