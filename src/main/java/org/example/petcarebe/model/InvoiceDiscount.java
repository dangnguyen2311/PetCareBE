package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "InvoiceDiscount")
@Data
public class InvoiceDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "applied_amount", nullable = false)
    private Double appliedAmount;

    @ManyToOne
    @JoinColumn(name = "Invoiceid", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "Discountid", nullable = false)
    private Discount discount;
}