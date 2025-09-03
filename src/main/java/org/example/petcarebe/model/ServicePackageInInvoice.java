package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Servicepackageininvoice")
@Data
public class ServicePackageInInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Invoiceid", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServicePackageid", nullable = false)
    private ServicePackage servicePackage;
}