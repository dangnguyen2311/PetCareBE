package org.example.petcarebe.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNotificationRequest {
    
    @NotBlank(message = "Message is required")
    private String message;
    
    @NotBlank(message = "Type is required")
    private String type; // APPOINTMENT, VACCINE, PAYMENT, GENERAL, REMINDER
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private Long vaccineScheduleId; // Optional, for vaccine-related notifications
}
