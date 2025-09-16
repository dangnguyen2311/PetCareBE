package org.example.petcarebe.dto.response.visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVisitResponse {
    private Long id;
    private LocalDate visitDate;
    private String reasonVisit;
    private LocalTime visitTime;
    private Integer queueNumber;
    private Integer roomNumber;
    private String notes;
    private Long workScheduleId;
    private String doctorName;
    private Long clinicRoomId;
    private String clinicRoomName;
    private Long petId;
    private String petName;
    private String message;
}
