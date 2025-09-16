package org.example.petcarebe.dto.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private LocalDate dateOfBirth;
}
