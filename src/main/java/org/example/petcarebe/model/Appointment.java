package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petcarebe.enums.AppointmentStatus;

@Entity
@Table(name = "Appointment")
@Data
public class Appointment {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private java.time.LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private java.time.LocalTime appointmentTime;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
//    private Status status = Status.PENDING;
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(name = "queue_number")
    private Integer queueNumber;

    @ManyToOne
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "WorkScheduleid")
    private WorkSchedule workSchedule;
}
