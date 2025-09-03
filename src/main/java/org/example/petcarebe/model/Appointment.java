package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "queue_number", nullable = false)
    private Integer queueNumber;

    @ManyToOne
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customer;
}
