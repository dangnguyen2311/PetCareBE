package org.example.petcarebe.dto.request.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDoctorRequest {
    
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullname;
    
    @Size(min = 2, max = 100, message = "Specialization must be between 2 and 100 characters")
    private String specialization;
    
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String gender;
    
    private LocalDate birthday;
}
