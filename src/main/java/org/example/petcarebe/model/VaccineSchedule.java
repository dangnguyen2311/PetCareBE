package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Vaccineschedule")
@Data
public class VaccineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Vaccineid")
    private Vaccine vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Petid", nullable = false)
    private Pet pet;
}