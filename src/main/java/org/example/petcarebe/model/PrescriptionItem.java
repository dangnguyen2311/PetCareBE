package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Prescriptionitem")
@Data
public class PrescriptionItem extends AbstractInvoiceItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "duration", nullable = false)
    private String duration;



    @Column(name = "instruction", nullable = false)
    private String instruction;

//    @Column(name = "price", nullable = false)
//    private Double price;
//    @Column(name = "quantity")
//    private Integer quantity;
//    @Column(name = "tax_percent", nullable = false)
//    private Double taxPercent;
//
//    @Column(name = "promotion_amount", nullable = false)
//    private Double promotionAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Prescriptionid", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Medicineid", nullable = false)
    private Medicine medicine;
}