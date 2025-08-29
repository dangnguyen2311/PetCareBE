package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ProductInInvoice")
@Data
public class ProductInInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "Invoiceid", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "Productid", nullable = false)
    private Product product;
}