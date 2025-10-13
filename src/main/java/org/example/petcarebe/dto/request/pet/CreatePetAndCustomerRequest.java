package org.example.petcarebe.dto.request.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePetAndCustomerRequest {
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
    private Long animalTypeId;
}
