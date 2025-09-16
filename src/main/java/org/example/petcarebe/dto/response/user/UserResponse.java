package org.example.petcarebe.dto.response.user;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private Boolean isDeleted;
    private String imgUrl;
    private String message;
}