package org.example.petcarebe.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ServiceInPromotion")
@Data
public class ServiceInPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Serviceid", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Promotionid", nullable = false)
    private Promotion promotion;
}