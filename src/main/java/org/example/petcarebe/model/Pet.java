package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Pet")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "datebirth", nullable = false)
    private LocalDate datebirth;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AnimalTypeid", nullable = false)
    private AnimalType animalType;

    @Column(name = "Customerid", nullable = false)
    private Integer customerId;
}