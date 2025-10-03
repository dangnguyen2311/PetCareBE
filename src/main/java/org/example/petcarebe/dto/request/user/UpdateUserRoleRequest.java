package org.example.petcarebe.dto.request.user;

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
public class UpdateUserRoleRequest {
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|USER|DOCTOR|STAFF)$", message = "Role must be one of: ADMIN, USER, DOCTOR, STAFF")
    private String role;
    
    private String reason; // Reason for role change
}
