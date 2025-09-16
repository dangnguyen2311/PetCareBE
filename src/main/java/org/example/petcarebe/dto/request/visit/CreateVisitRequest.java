package org.example.petcarebe.dto.request.visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.repository.VisitRepository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVisitRequest {
    private Long appointmentId;
    private Long workScheduleId;
    private Long clinicRoomId;
    private Long petId;
    private String reasonVisit;
    private Integer roomNumber;
    private String notes;
}
