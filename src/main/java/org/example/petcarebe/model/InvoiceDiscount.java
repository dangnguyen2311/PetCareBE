package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Invoicediscount")
@Data
public class InvoiceDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "applied_amount", nullable = false)
    private Double appliedAmount;

    @ManyToOne
    @JoinColumn(name = "Invoiceid", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "Discountid", nullable = false)
    private Discount discount;
}