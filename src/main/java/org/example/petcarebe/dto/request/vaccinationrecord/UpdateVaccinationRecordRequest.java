package org.example.petcarebe.dto.request.vaccinationrecord;

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
public class UpdateVaccinationRecordRequest {
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private String notes;
    private String status;
    private Long visitId;
    private Long vaccineId;
    private Long petId;
}
