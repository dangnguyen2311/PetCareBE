package org.example.petcarebe.dto.response.visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitResponse {
    private Long id;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reasonVisit;
    private Integer roomNumber;
    private String notes;
    
    // Work Schedule information
    private Long workScheduleId;
    private String doctorName;
    private String doctorSpecialization;
    private String doctorPhone;
    
    // Clinic Room information
    private Long clinicRoomId;
    private String clinicRoomName;
    
    // Pet information
    private Long petId;
    private String petName;
    private String petBreed;
    private String petType;
    
    // Customer information
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Appointment information (if exists)
    private Long appointmentId;
    private Integer queueNumber;
    private String appointmentStatus;
    
    // Medical summary flags
    private Boolean hasDiagnoses;
    private Boolean hasTestResults;
    private Boolean hasPrescriptions;
    
    // Response message
    private String message;
}
