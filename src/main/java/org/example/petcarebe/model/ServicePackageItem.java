package org.example.petcarebe.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ServicePackageItem")
@Data
public class ServicePackageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Serviceid", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServicePackageid", nullable = false)
    private ServicePackage servicePackage;
}