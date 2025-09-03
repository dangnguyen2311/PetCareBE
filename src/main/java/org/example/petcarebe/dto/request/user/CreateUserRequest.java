package org.example.petcarebe.dto.request.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 3 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role; // ADMIN, USER, DOCTOR, STAFF
}