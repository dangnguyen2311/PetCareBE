package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Vaccineininvoice")
@Data
public class VaccineInInvoice extends AbstractInvoiceItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "tax_percent", nullable = false)
    private Double taxPercent;

    @Column(name = "promotion_amount", nullable = false)
    private Double promotionAmount;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Vaccineid", nullable = false)
    private Vaccine vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Invoiceid", nullable = false)
    private Invoice invoice;
}