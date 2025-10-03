package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.inventoryitem.CreateInventoryItemRequest;
import org.example.petcarebe.dto.request.inventoryitem.UpdateInventoryItemRequest;
import org.example.petcarebe.dto.response.inventoryitem.CreateInventoryItemResponse;
import org.example.petcarebe.dto.response.inventoryitem.InventoryItemResponse;
import org.example.petcarebe.dto.response.inventoryitem.UpdateInventoryItemResponse;
import org.example.petcarebe.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/inventory-items")
public class InventoryItemController {
    @Autowired
    private InventoryItemService inventoryItemService;

    @PostMapping
    public ResponseEntity<CreateInventoryItemResponse> createNewInventoryItem(@RequestBody CreateInventoryItemRequest request) {
        try{
            CreateInventoryItemResponse response = inventoryItemService.createInventoryItem(request);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            CreateInventoryItemResponse error =  new CreateInventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            CreateInventoryItemResponse error =  new CreateInventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{inventoryItemId}")
    public ResponseEntity<InventoryItemResponse> getInventoryItem(@PathVariable("inventoryItemId") Long inventoryItemId) {
        try{
            InventoryItemResponse response = inventoryItemService.getInventoryItemById(inventoryItemId);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            InventoryItemResponse error =  new InventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            InventoryItemResponse error =  new InventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{inventoryItemId}")
    public ResponseEntity<UpdateInventoryItemResponse> updateInventoryItem(@PathVariable Long inventoryItemId ,@RequestBody UpdateInventoryItemRequest request){
        try{
            UpdateInventoryItemResponse response = inventoryItemService.updateInventoryItem(inventoryItemId, request);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            UpdateInventoryItemResponse error =  new UpdateInventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            UpdateInventoryItemResponse error =  new UpdateInventoryItemResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    @GetMapping("/getlist")
    public ResponseEntity<List<InventoryItemResponse>> getInventoryItemList(){
        try{
            List<InventoryItemResponse> inventoryItemResponseList = inventoryItemService.getAllInventoryItem();
            return ResponseEntity.status(HttpStatus.OK).body(inventoryItemResponseList);
        }
        catch (RuntimeException e) {
            List<InventoryItemResponse> error =  new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            List<InventoryItemResponse> error =  new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
