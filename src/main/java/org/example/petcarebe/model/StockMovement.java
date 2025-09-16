package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "stockmovement")
@Data
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movement_type")
    private String movementType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "movement_date")
    private Date movementDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InventoryItemid")
    private InventoryItem inventoryItem;
}
