package org.example.petcarebe.dto.request.visit;

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
public class UpdateVisitRequest {
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reasonVisit;
    private Integer roomNumber;
    private String notes;
    private Long workScheduleId;
    private Long clinicRoomId;
}
