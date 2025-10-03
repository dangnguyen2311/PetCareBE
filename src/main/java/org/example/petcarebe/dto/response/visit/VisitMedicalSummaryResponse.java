package org.example.petcarebe.dto.response.visit;

import org.example.petcarebe.dto.response.diagnosis.DiagnosisResponse;
import org.example.petcarebe.dto.response.testresult.TestResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitMedicalSummaryResponse {
    // Visit basic information
    private Long visitId;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reasonVisit;
    private String notes;
    
    // Pet information
    private Long petId;
    private String petName;
    private String petBreed;
    private String petType;
    
    // Customer information
    private Long customerId;
    private String customerName;
    
    // Doctor information
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    
    // Medical records
    private List<DiagnosisResponse> diagnoses;
    private List<TestResultResponse> testResults;
    
    // Summary statistics
    private Integer totalDiagnoses;
    private Integer totalTestResults;
    
    // Response message
    private String message;
}
