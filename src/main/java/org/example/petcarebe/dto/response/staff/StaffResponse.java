package org.example.petcarebe.dto.response.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponse {
    private Long id;
    private String fullname;
    private String position;
    private String phone;
    private String email;
    private String address;
    
    // User information
    private Long userId;
    private String username;
    private String role;
    private Boolean isDeleted;
    private String imgUrl;
    
    // Additional information
    private String message;
}
