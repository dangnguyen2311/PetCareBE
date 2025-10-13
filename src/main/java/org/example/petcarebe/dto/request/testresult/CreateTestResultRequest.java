package org.example.petcarebe.dto.request.testresult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTestResultRequest {
    
    @NotNull(message = "Visit ID is required")
    private Long visitId;
    
    @NotBlank(message = "Test type is required")
    @Size(min = 2, max = 100, message = "Test type must be between 2 and 100 characters")
    private String testType;

    private String status;

    @NotBlank(message = "Result is required")
    @Size(min = 1, max = 1000, message = "Result must be between 1 and 1000 characters")
    private String result;


    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
