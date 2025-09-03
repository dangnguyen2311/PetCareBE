package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "animaltype")
@Data
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "imgUrl")
    private String imgUrl;
}