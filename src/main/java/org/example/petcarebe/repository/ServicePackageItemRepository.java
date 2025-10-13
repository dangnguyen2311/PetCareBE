package org.example.petcarebe.repository;

import org.example.petcarebe.model.ServicePackage;
import org.example.petcarebe.model.ServicePackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicePackageItemRepository extends JpaRepository<ServicePackageItem, Integer> {
    List<ServicePackageItem> findAllByServicePackage(ServicePackage servicePackage);
}
