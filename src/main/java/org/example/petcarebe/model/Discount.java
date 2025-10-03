package org.example.petcarebe.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "discount")
@Data
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(name = "type", nullable = false) //PERCENT, CASH
    private String type;

    @Column(nullable = false)
    private Double value;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date",nullable = false)
    private LocalDateTime endDate;

    @Column(name = "max_amount",nullable = false)
    private Double maxAmount;

    @Column(name = "min_amount",nullable = false)
    private Double minAmount;

    @Column(nullable = false)
    private String status;

    @Column(name = "isDeleted")
    private Boolean isDeleted;
}
