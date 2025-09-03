package org.example.petcarebe.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Vaccine")
@Data
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;
}