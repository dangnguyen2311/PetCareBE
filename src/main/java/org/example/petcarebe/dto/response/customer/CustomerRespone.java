package org.example.petcarebe.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRespone {
    private Long id;
    private String clientId;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private LocalDate dateOfBirth;
    private String status;
    private String message;
}
