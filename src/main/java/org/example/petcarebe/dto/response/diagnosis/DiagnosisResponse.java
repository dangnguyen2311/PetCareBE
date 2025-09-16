package org.example.petcarebe.dto.response.diagnosis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiagnosisResponse {
    
    private Long id;
    private String description;
    private LocalDate createdDate;
    
    // Visit information
    private Long visitId;
    private LocalDate visitDate;
    private String visitNotes;
    
    // Disease information
    private Long diseaseId;
    private String diseaseName;
    private String diseaseDescription;
    
    // Pet information (from visit)
    private Long petId;
    private String petName;
    private String petType;
    
    // Customer information (from visit -> pet)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    
    // Doctor information (from visit)
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    
    // Response message
    private String message;
}
