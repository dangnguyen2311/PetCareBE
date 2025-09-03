package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Servicepackageinpromotion")
@Data
public class ServicePackageInPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Promotionid", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "ServicePackageid", nullable = false)
    private ServicePackage servicePackage;
}