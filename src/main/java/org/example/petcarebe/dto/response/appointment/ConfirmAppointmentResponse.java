package org.example.petcarebe.dto.response.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmAppointmentResponse {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String notes;
    private AppointmentStatus status;
    private Integer queueNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String doctorName;


}
