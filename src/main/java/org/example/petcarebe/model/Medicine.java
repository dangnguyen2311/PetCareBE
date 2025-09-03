package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Medicine")
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "notes")
    private String notes;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;
}