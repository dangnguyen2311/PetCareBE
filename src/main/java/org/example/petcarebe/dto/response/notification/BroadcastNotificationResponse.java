package org.example.petcarebe.dto.response.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BroadcastNotificationResponse {
    private Long id;
    private String message;
    private String type; // APPOINTMENT, VACCINE, PAYMENT, GENERAL, REMINDER
    private LocalDate createdDate;
    private String status;
    private Boolean isRead;
    private String responseMessage;
    private LocalDateTime createdAt;
    //Bo sung
    private String timeStamp;
    private String title;

}
