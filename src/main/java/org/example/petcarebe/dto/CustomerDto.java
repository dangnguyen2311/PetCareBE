package org.example.petcarebe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String fullname;
    private String phone;
    private String email;
    private String address;
    private String username;
    private String password;
    private LocalDate createdDate;
    private String status;
    private Long userId;
}

