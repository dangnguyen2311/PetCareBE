package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.inventoryitem.CreateInventoryItemRequest;
import org.example.petcarebe.dto.response.inventoryitem.InventoryItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/inventory-item")
public class InventoryItemController {

    @PostMapping
    public ResponseEntity<InventoryItemResponse> createNewInventoryItem(@RequestBody CreateInventoryItemRequest inventoryItem) {
        return ResponseEntity.ok(null);
    }
}
