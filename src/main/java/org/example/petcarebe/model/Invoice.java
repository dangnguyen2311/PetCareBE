package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Invoice")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.UNPAID;

    @Column(name = "total_discount_amount", nullable = false)
    private Double totalDiscountAmount;

    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;

    @ManyToOne
    @JoinColumn(name = "Staffid", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "Customerid", nullable = false)
    private Customer customerId;

    public enum Status {
        UNPAID, PAID, CANCELLED
    }
}