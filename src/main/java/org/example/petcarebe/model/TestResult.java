package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Testresult")
@Data
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_type", nullable = false)
    private String testType;     //BLOOD_TEST, URINE_TEST, FECAL_TEST, PARASITOLOGY, POINT_OF_CARE, IMAGING

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "status", nullable = false)
    private String status;  // NORMAL, ABNORMAL

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "Visitid", nullable = false)
    private Visit visit;
}