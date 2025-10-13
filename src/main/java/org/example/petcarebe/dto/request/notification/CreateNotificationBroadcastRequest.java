package org.example.petcarebe.dto.request.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNotificationBroadcastRequest {
    private String message;
    private String type; // APPOINTMENT, VACCINE, PAYMENT, GENERAL, REMINDER
    private String title;
}
