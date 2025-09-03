package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Diagnosis")
@Data
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_date", nullable = false)
    private java.time.LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Visitid", nullable = false)
    private Visit visit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Diseaseid", nullable = false)
    private Disease disease;
}
