package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "PetWeightRecord")
@Data
public class PetWeightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MedicalRecordid", nullable = false)
    private MedicalRecord medicalRecord;
}