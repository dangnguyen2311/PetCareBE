package org.example.petcarebe.dto.response.vaccinationrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccinationRecordResponse {
    private Long vaccinationRecordId;
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private String status; // SCHEDULED, PENDING, COMPLETED, MISSED, CANCELLED, OVERREACTED
    private String notes;
    private String doctorName;
    private String vaccineName;
    private String vaccineManufacture;
    private String vaccineDescription;
    private String petName;
    private String petType;
    private Long visitId;
    private String message;
}
