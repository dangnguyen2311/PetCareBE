package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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

    @Column(name = "reason_visit", nullable = false)
    private String reasonVisit;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WorkScheduleid", nullable = false)
    private WorkSchedule workScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClinicRoomid", nullable = false)
    private ClinicRoom clinicRoomId;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointmentid")
    private Appointment appointment;

    @Column(name = "Petid", nullable = false)
    private Integer petId;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses;
}