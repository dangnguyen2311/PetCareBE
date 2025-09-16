package org.example.petcarebe.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private Long id;
    private String fullname;
    private String specialization;
    private String phone;
    private String email;
    private String gender;
    private LocalDate birthday;
    
    // User information
    private Long userId;
    private String username;
    private String userRole;
    private Boolean isDeleted;
    
    // Additional information
    private String message;
}
