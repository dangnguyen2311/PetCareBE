package org.example.petcarebe.dto.response.notification;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class SocketNotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type; // info, success, warning, error, appointment, message
    private String timestamp;
    private Map<String, Object> data;

    public SocketNotificationResponse() {
        this.timestamp = Instant.now().toString();
    }

    public SocketNotificationResponse(String title, String message, String type) {
        this();
        this.title = title;
        this.message = message;
        this.type = type;
    }
}
