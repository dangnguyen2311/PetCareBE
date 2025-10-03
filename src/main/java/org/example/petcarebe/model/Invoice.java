package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petcarebe.enums.InvoiceStatus;

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

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "customerPhone")
    private String customerPhone;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status =  InvoiceStatus.DRAFT;

    @Column(name = "total_discount_amount")
    private Double totalDiscountAmount;

    @Column(name = "final_amount")
    private Double finalAmount;

    @Column(name = "tax_total_amount")
    private Double taxTotalAmount;

    @ManyToOne
    @JoinColumn(name = "Staffid")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "Customerid")
    private Customer customer;

    public enum Status {
        UNPAID, PAID, CANCELLED
    }
}