package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Productinpromotion")
@Data
public class ProductInPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Productid", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Promotionid", nullable = false)
    private Promotion promotion;
}