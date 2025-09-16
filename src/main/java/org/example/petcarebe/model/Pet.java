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
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "breed")
    private String breed;

    @Column(name = "gender")
    private String gender;

    @Column(name = "datebirth")
    private LocalDate datebirth;

    @Column(name = "color")
    private String color;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AnimalTypeid")
    private AnimalType animalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customer;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "isNeuteredOrSpayed")
    private Boolean isNeuteredOrSpayed;
}