package org.example.petcarebe.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String role;
    private String message;
}