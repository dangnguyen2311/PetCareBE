package org.example.petcarebe.dto.request.animaltype;

import lombok.Data;

@Data
public class CreateAnimalTypeRequest {
    private String name;
    private String description;
}
