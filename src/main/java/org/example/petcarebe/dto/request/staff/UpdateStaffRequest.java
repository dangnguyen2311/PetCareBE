package org.example.petcarebe.dto.request.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffRequest {
    
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullname;
    
    @Size(min = 2, max = 50, message = "Position must be between 2 and 50 characters")
    private String position;
    
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phone;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;
}
