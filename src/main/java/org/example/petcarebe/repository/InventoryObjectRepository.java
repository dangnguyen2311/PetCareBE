package org.example.petcarebe.repository;

import org.example.petcarebe.model.InventoryObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryObjectRepository extends JpaRepository<InventoryObject, Integer> {
}
