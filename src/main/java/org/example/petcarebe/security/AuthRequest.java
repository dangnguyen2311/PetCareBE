package org.example.petcarebe.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Authentication request containing user credentials")
public class AuthRequest {

    @Schema(description = "Username for authentication", example = "admin", required = true)
    private String username;

    @Schema(description = "Password for authentication", example = "password123", required = true)
    private String password;

}