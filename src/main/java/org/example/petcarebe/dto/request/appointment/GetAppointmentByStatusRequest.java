package org.example.petcarebe.dto.request.appointment;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.enums.AppointmentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAppointmentByStatusRequest {
    private AppointmentStatus status;
    private Long customerId;
}
