package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Vaccineinpromotion")
@Data
public class VaccineInPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Vaccineid", nullable = false)
    private Vaccine vaccine;

    @ManyToOne
    @JoinColumn(name = "Promotionid", nullable = false)
    private Promotion promotion;
}