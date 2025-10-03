package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.inventoryitem.CreateInventoryItemRequest;
import org.example.petcarebe.dto.request.inventoryitem.UpdateInventoryItemRequest;
import org.example.petcarebe.dto.response.inventoryitem.CreateInventoryItemResponse;
import org.example.petcarebe.dto.response.inventoryitem.InventoryItemResponse;
import org.example.petcarebe.dto.response.inventoryitem.UpdateInventoryItemResponse;
import org.example.petcarebe.enums.InventoryItemType;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.model.InventoryItem;
import org.example.petcarebe.model.InventoryObject;
import org.example.petcarebe.repository.InventoryItemRepository;
import org.example.petcarebe.repository.InventoryObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryItemService {
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    @Autowired
    private InventoryObjectRepository  inventoryObjectRepository;


    public CreateInventoryItemResponse createInventoryItem(CreateInventoryItemRequest request) {
        InventoryObject inventoryObject = inventoryObjectRepository.findById(request.getInventoryObjectId())
                .orElseThrow(() -> new RuntimeException("Inventory Object Not Found"));
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setInventoryObject(inventoryObject);
        inventoryItem.setQuantity(request.getQuantity());
        inventoryItem.setUnit(request.getUnit());
        inventoryItem.setMinQuantity(request.getMinQuantity());
        inventoryItem.setInventoryType(setInventoryItemType(inventoryObject.getType()));
        inventoryItem.setName(request.getName());
        inventoryItem.setCreatedAt(LocalDateTime.now());
        inventoryItem.setUpdatedAt(LocalDateTime.now());

        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);
        return convertToCreateInventoryItenResponse(savedInventoryItem);

    }
    private InventoryItemType setInventoryItemType(InventoryObjectType inventoryObjectType) {
        return switch (inventoryObjectType){
            case PRODUCT ->  InventoryItemType.PRODUCT;
            case VACCINE -> InventoryItemType.VACCINE;
            case MEDICINE ->  InventoryItemType.MEDICINE;
            default -> null;
        };
    }

    public List<InventoryItemResponse> getAllInventoryItem() {
        return inventoryItemRepository.findAll(Sort.by(Sort.Direction.ASC, "inventoryObjectId"))
                .stream().map(this::convertToInventoryItemResponse)
                .toList();
    }

    public InventoryItemResponse getInventoryItemById(Long inventoryItemId) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId).
                orElseThrow(() -> new RuntimeException("Inventory Item Not Found"));
        return convertToInventoryItemResponse(inventoryItem, "Inventory Item created successfully");
    }

    public UpdateInventoryItemResponse updateInventoryItem(Long inventoryItemId, UpdateInventoryItemRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(()  -> new RuntimeException("Inventory Item Not Found"));
        inventoryItem.setName(request.getName());
        inventoryItem.setMinQuantity(request.getMinQuantity());
        inventoryItem.setUnit(request.getUnit());
        inventoryItem.setUpdatedAt(LocalDateTime.now());
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);
        return convertToUpdateInventoryItemResponse(savedInventoryItem);

    }

    private InventoryItemResponse convertToInventoryItemResponse(InventoryItem inventoryItem) {
        return InventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .quantity(inventoryItem.getQuantity())
                .name(inventoryItem.getName())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .updatedAt(inventoryItem.getUpdatedAt())
                .inventoryObjectId(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getId() : null)
                .inventoryObjectName(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getName() : null)
                .inventoryObjectDescription(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getDescription() : null)
                .inventoryObjectType(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getType() : null)
                .message("")
                .build();

    }
    private InventoryItemResponse convertToInventoryItemResponse(InventoryItem inventoryItem, String message) {
        return InventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .quantity(inventoryItem.getQuantity())
                .name(inventoryItem.getName())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .updatedAt(inventoryItem.getUpdatedAt())
                .inventoryObjectId(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getId() : null)
                .inventoryObjectName(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getName() : null)
                .inventoryObjectDescription(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getDescription() : null)
                .inventoryObjectType(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getType() : null)
                .message(message)
                .build();

    }


    private CreateInventoryItemResponse convertToCreateInventoryItenResponse(InventoryItem inventoryItem){
        return CreateInventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .name(inventoryItem.getName())
                .quantity(inventoryItem.getQuantity())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .inventoryObjectId(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getId() : null)
                .message("")
                .build();
    }
    private CreateInventoryItemResponse convertToCreateInventoryItenResponse(InventoryItem inventoryItem, String message){
        return CreateInventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .name(inventoryItem.getName())
                .quantity(inventoryItem.getQuantity())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .inventoryObjectId(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getId() : null)
                .message(message)
                .build();
    }

    private UpdateInventoryItemResponse convertToUpdateInventoryItemResponse(InventoryItem inventoryItem){
        return UpdateInventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .name(inventoryItem.getName())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .updatedAt(inventoryItem.getUpdatedAt())
                .inventoryObjectName(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getName() : null)
                .inventoryObjectType(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getType() : null)
                .message("")
                .build();

    }

    private UpdateInventoryItemResponse convertToUpdateInventoryItemResponse(InventoryItem inventoryItem, String message){
        return UpdateInventoryItemResponse.builder()
                .id(inventoryItem.getId())
                .name(inventoryItem.getName())
                .minQuantity(inventoryItem.getMinQuantity())
                .unit(inventoryItem.getUnit())
                .createdAt(inventoryItem.getCreatedAt())
                .updatedAt(inventoryItem.getUpdatedAt())
                .inventoryObjectName(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getName() : null)
                .inventoryObjectType(inventoryItem.getInventoryObject() != null ? inventoryItem.getInventoryObject().getType() : null)
                .message(message)
                .build();

    }



}
