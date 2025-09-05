package org.example.petcarebe.dto.response.animaltype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalTypeResponse {
    private Long id;
    private String name;
    private String description;
    private String message;
}
