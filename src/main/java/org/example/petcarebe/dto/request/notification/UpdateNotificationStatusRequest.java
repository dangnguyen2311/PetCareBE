package org.example.petcarebe.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateNotificationStatusRequest {
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(UNREAD|READ|ARCHIVED)$", message = "Status must be one of: UNREAD, READ, ARCHIVED")
    private String status;
}
