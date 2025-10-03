package org.example.petcarebe.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    PENDING("PENDING", "Appointment is waiting for confirmation"),
    CONFIRMED("CONFIRMED", "Appointment has been confirmed"),
    INPROGRESS("INPROGRESS", "Appointment is currently in progress"),
    COMPLETED("COMPLETED", "Appointment has been completed successfully"),
    CANCELLED("CANCELLED", "Appointment has been cancelled"),
    NOSHOW("NOSHOW", "Customer did not show up for the appointment"),
    RESCHEDULED("RESCHEDULED", "Appointment has been rescheduled to another time");

    private final String displayName;
    private final String description;

    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
