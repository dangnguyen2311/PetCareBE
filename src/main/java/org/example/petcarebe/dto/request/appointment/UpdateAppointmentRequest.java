package org.example.petcarebe.dto.request.appointment;

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
public class UpdateAppointmentRequest {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String notes;
}
