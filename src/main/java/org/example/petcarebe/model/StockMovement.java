package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petcarebe.enums.StockMovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "stockmovement")
@Data
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movement_type")
    @Enumerated(EnumType.STRING)
    private StockMovementType movementType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "InventoryItemid")
    private InventoryItem inventoryItem;
}
