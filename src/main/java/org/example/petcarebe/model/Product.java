package org.example.petcarebe.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "imgUrl")
    private String imgUrl;

    @OneToOne
    @JoinColumn(name = "inventory_object_id")
    private InventoryObject inventoryObject;
}