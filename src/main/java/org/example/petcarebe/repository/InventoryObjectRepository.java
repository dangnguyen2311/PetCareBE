package org.example.petcarebe.repository;

import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.model.InventoryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryObjectRepository extends JpaRepository<InventoryObject, Long> {
    List<InventoryObject> findByType(InventoryObjectType type);
    List<InventoryObject> findByNameContainingIgnoreCase(String name);
}
