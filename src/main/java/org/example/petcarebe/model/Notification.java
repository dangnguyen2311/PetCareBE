package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Notification")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VaccineScheduleid")
    private VaccineSchedule vaccineSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customer;
}
