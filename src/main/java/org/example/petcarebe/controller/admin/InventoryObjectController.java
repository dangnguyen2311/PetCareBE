package org.example.petcarebe.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.petcarebe.dto.request.inventoryobject.CreateInventoryObjectRequest;
import org.example.petcarebe.dto.request.inventoryobject.UpdateInventoryObjectRequest;
import org.example.petcarebe.dto.response.inventoryobject.InventoryObjectResponse;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.service.InventoryObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/inventory-objects")
@Tag(name = "📦 Inventory Objects Management", description = "Admin endpoints for managing inventory object types")
public class InventoryObjectController {
    @Autowired
    private InventoryObjectService inventoryObjectService;

    @Operation(
            summary = "Create new inventory object",
            description = "Create a new inventory object type (Medicine, Product, Vaccine, Equipment, Supply)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory object created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<InventoryObjectResponse> createInventoryObject(@Valid @RequestBody CreateInventoryObjectRequest request) {
        InventoryObjectResponse createdInventoryObject = inventoryObjectService.createInventoryObject(request);
        return ResponseEntity.ok(createdInventoryObject);
    }

    @Operation(
            summary = "Get all inventory objects",
            description = "Retrieve a list of all inventory object types"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory objects retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<InventoryObjectResponse>> getAllInventoryObjects() {
        List<InventoryObjectResponse> inventoryObjects = inventoryObjectService.getAllInventoryObjects();
        return ResponseEntity.ok(inventoryObjects);
    }

    @Operation(
            summary = "Get inventory object by ID",
            description = "Retrieve a specific inventory object by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory object retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory object not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryObjectResponse> getInventoryObjectById(
            @Parameter(description = "ID of the inventory object", example = "1", required = true)
            @PathVariable Long id) {
        InventoryObjectResponse inventoryObject = inventoryObjectService.getInventoryObjectById(id);
        return ResponseEntity.ok(inventoryObject);
    }

    @Operation(
            summary = "Update inventory object",
            description = "Update an existing inventory object by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory object updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Inventory object not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventoryObjectResponse> updateInventoryObject(
            @Parameter(description = "ID of the inventory object", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryObjectRequest request) {
        InventoryObjectResponse updatedInventoryObject = inventoryObjectService.updateInventoryObject(id, request);
        return ResponseEntity.ok(updatedInventoryObject);
    }

    @Operation(
            summary = "Delete inventory object",
            description = "Delete an inventory object by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventory object deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory object not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryObject(
            @Parameter(description = "ID of the inventory object", example = "1", required = true)
            @PathVariable Long id) {
        inventoryObjectService.deleteInventoryObject(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get inventory objects by type",
            description = "Retrieve inventory objects filtered by type (MEDICINE, PRODUCT, VACCINE, EQUIPMENT, SUPPLY)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory objects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid type parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<InventoryObjectResponse>> getInventoryObjectsByType(
            @Parameter(description = "Type of inventory object", example = "MEDICINE", required = true)
            @PathVariable InventoryObjectType type) {
        List<InventoryObjectResponse> inventoryObjects = inventoryObjectService.getInventoryObjectsByType(type);
        return ResponseEntity.ok(inventoryObjects);
    }

    @Operation(
            summary = "Search inventory objects by name",
            description = "Search inventory objects by name (case-insensitive partial match)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<InventoryObjectResponse>> searchInventoryObjectsByName(
            @Parameter(description = "Name to search for", example = "medicine", required = true)
            @RequestParam String name) {
        List<InventoryObjectResponse> inventoryObjects = inventoryObjectService.searchInventoryObjectsByName(name);
        return ResponseEntity.ok(inventoryObjects);
    }
}
