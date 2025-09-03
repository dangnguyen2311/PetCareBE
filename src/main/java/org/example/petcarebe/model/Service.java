package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Service")
@Data
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "category")
    private String category;

    @Column(name = "created_at")
    private LocalDate createdDate;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

}