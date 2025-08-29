package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "TestResult")
@Data
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "test_type", nullable = false)
    private String testType;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "Visitid", nullable = false)
    private Visit visit;
}