package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petcarebe.enums.InventoryItemType;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventoryitem")
@Data
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "name")
    private String name;

    @Column(name = "min_quantity")
    private Integer minQuantity;

    @Column(name = "unit")
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_type")
    private InventoryItemType inventoryType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InventoryObjectid")
    private InventoryObject inventoryObject;
}
