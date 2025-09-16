package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Visit")
@Data
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visit_time")
    private LocalTime visitTime;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "reason_visit", nullable = false)
    private String reasonVisit;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WorkScheduleid", nullable = false)
    private WorkSchedule workSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ClinicRoomid", nullable = false)
    private ClinicRoom clinicRoom;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointmentid")
    private Appointment appointment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Petid", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses;
}