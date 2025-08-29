package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "PetFoodRecord")
@Data
public class PetFoodRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "food_type", nullable = false)
    private String foodType;

    @Column(name = "amount_food", nullable = false)
    private String amountFood;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MedicalRecordid", nullable = false)
    private MedicalRecord medicalRecord;
}