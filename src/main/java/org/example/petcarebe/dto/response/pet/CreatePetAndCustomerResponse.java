package org.example.petcarebe.dto.response.pet;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePetAndCustomerResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private String customerGender;
    private LocalDate customerBirthday;
    private String name;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private String animalTypeName;
    private String message;
}
