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
public class NotificationResponse {
    private Long id;
    private String message;
    private String type;
    private LocalDate createdDate;
    private String status;
    private String title;
    private Long userId;
    private Boolean isRead;
    private String responseMessage;
    private LocalDateTime createdAt;

    
//    // Customer information
//    private Long customerId;
//    private String customerName;
//    private String customerEmail;
//
//    // Vaccine schedule information (if applicable)
//    private Long vaccineScheduleId;
//    private String vaccineScheduleDetails;
}
