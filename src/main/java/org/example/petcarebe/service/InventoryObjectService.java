package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.inventoryobject.CreateInventoryObjectRequest;
import org.example.petcarebe.dto.request.inventoryobject.UpdateInventoryObjectRequest;
import org.example.petcarebe.dto.response.inventoryobject.InventoryObjectResponse;
import org.example.petcarebe.model.InventoryObject;
import org.example.petcarebe.repository.InventoryObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryObjectService {
    @Autowired
    private InventoryObjectRepository inventoryObjectRepository;

    public InventoryObjectResponse createInventoryObject(CreateInventoryObjectRequest request) {
        InventoryObject inventoryObject = new InventoryObject();
        inventoryObject.setName(request.getName());
        inventoryObject.setDescription(request.getDescription());
        inventoryObject.setType(request.getType());

        InventoryObject savedInventoryObject = inventoryObjectRepository.save(inventoryObject);
        return convertToResponse(savedInventoryObject);
    }

    public List<InventoryObjectResponse> getAllInventoryObjects() {
        return inventoryObjectRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public InventoryObjectResponse getInventoryObjectById(Long id) {
        InventoryObject inventoryObject = inventoryObjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InventoryObject not found with id: " + id));
        return convertToResponse(inventoryObject);
    }

    public InventoryObjectResponse updateInventoryObject(Long id, UpdateInventoryObjectRequest request) {
        InventoryObject inventoryObject = inventoryObjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InventoryObject not found with id: " + id));

        inventoryObject.setName(request.getName());
        inventoryObject.setDescription(request.getDescription());
        inventoryObject.setType(request.getType());

        InventoryObject updatedInventoryObject = inventoryObjectRepository.save(inventoryObject);
        return convertToResponse(updatedInventoryObject);
    }

    public void deleteInventoryObject(Long id) {
        InventoryObject inventoryObject = inventoryObjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InventoryObject not found with id: " + id));
        inventoryObjectRepository.delete(inventoryObject);
    }

    public List<InventoryObjectResponse> getInventoryObjectsByType(org.example.petcarebe.enums.InventoryObjectType type) {
        return inventoryObjectRepository.findByType(type).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<InventoryObjectResponse> searchInventoryObjectsByName(String name) {
        return inventoryObjectRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private InventoryObjectResponse convertToResponse(InventoryObject inventoryObject) {
        return new InventoryObjectResponse(
                inventoryObject.getId(),
                inventoryObject.getName(),
                inventoryObject.getDescription(),
                inventoryObject.getType(),
                inventoryObject.getType().getDisplayName(),
                inventoryObject.getType().getDescription()
        );
    }
}
