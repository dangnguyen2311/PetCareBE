package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "stockmovement_productininvoice")
@Data
public class StockMovement_ProductInInvoice  { //implements Serializable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StockMovementid")
    private StockMovement stockMovement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_in_invoice_id")
    private ProductInInvoice productInInvoice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

