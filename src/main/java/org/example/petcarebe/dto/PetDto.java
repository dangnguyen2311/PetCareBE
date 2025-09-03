package org.example.petcarebe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private Long animalTypeId;
    private Long customerId;
}

