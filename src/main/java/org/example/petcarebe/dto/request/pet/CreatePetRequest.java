package org.example.petcarebe.dto.request.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePetRequest {
    private String name;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private Long animalTypeId;  // camelCase
    @JsonProperty(value = "customerId")
    private Long customerId;    // camelCase
}
