package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petcarebe.enums.InventoryObjectType;

@Entity
@Table(name = "inventoryobject")
@Data

public class InventoryObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InventoryObjectType type;
}
