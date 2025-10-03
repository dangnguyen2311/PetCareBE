package org.example.petcarebe.repository;

import org.example.petcarebe.model.InventoryItem;
import org.example.petcarebe.model.InventoryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem,Long> {
    Optional<InventoryItem> findByInventoryObject(InventoryObject inventoryObject);
}
