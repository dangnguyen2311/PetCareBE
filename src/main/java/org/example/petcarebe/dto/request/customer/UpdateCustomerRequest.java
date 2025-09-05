package org.example.petcarebe.dto.request.customer;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateCustomerRequest {
    private String fullName;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
}
