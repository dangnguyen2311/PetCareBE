package org.example.petcarebe.dto.request.disease;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDiseaseRequest {
    
    @Size(min = 2, max = 100, message = "Disease name must be between 2 and 100 characters")
    private String name;
    
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
}
