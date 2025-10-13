package org.example.petcarebe.dto.response.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private String breed;
    private String imgUrl;

    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private Long animalTypeId;
    private String animalTypeName; // Example of adding more useful data
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
}

