package org.example.petcarebe.dto.request.vaccinationrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVaccinationRecordRequest {
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private String notes;
    private Long doctorId;
    private Long visitId;
    private Long vaccineId;
    private Long petId;
}
