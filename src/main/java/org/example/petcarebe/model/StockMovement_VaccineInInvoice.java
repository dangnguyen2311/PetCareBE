package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "stockmovement_vaccineininvoice")
@Data
public class StockMovement_VaccineInInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StockMovementid")
    private StockMovement stockMovement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_in_invoice_id")
    private VaccineInInvoice vaccineInInvoice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
