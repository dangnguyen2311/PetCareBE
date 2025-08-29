package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PrescriptionItem")
@Data
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "instruction", nullable = false)
    private String instruction;

    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Prescriptionid", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Medicineid", nullable = false)
    private Medicine medicine;
}