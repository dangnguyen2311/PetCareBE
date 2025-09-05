package org.example.petcarebe.dto.request.pet;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePetRequest {
    private String name;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private Long AnimalTypeId;
}

