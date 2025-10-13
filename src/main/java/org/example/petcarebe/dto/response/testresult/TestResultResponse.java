package org.example.petcarebe.dto.response.testresult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultResponse {
    
    private Long id;
    private String testType; //BLOOD_TEST, URINE_TEST, FECAL_TEST, PARASITOLOGY, POINT_OF_CARE, IMAGING
    private String result;
    private LocalDate createdDate;
    private String notes;
    private String status; //NORMAL, ABNORMAL
    
    // Visit information
    private Long visitId;
    private LocalDate visitDate;
    private String visitNotes;
    
    // Pet information (from visit)
    private Long petId;
    private String petName;
    private String petType;
    private String petBreed;
    
    // Customer information (from visit -> pet)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Doctor information (from visit)
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    
    // Response message
    private String message;
}
