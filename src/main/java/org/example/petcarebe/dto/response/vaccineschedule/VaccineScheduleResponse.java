package org.example.petcarebe.dto.response.vaccineschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineScheduleResponse {
    
    private Long id;
    private LocalDate scheduledDate;
    private String status;
    private String notes;
    
    // Vaccine information
    private Long vaccineId;
    private String vaccineName;
    private String vaccineManufacturer;
    private String vaccineDescription;
    
    // Pet information
    private Long petId;
    private String petName;
    private String petBreed;
    private String petType;

    
    // Response message
    private String message;
}
