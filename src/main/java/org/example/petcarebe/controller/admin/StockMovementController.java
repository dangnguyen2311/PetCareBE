package org.example.petcarebe.controller.admin;

import jakarta.websocket.server.PathParam;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementPrescriptionItemRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementProductRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementVaccineRequest;
import org.example.petcarebe.dto.response.stockmovement.*;
import org.example.petcarebe.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/stock-movement")
public class StockMovementController {

    @Autowired
    private StockMovementService  stockMovementService;

    @PostMapping("/general") // Theem loai mawjt hangf caanf theem vaof sau: StockMovement_PrescriptionItem, StockMovement_ProductInInvoice, StockMovement_VaccineInInvoice
    public ResponseEntity<CreateStockMovementResponse> createStockMovement(@RequestBody CreateStockMovementRequest request){
        try{
            CreateStockMovementResponse response = stockMovementService.createStockMovement(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            CreateStockMovementResponse errorResponse = new CreateStockMovementResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            CreateStockMovementResponse errorResponse = new CreateStockMovementResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @GetMapping("/get-list")
    public ResponseEntity<List<StockMovementResponse>> getAllStockMovements(){
        try{
            List<StockMovementResponse> responses = stockMovementService.getAllStockMovements();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (RuntimeException e) {
            List<StockMovementResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            List<StockMovementResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{stockMovementId}")
    public ResponseEntity<StockMovementResponse> getStockMovementById(@PathVariable Long stockMovementId){
        try {
            StockMovementResponse stockMovementResponse = stockMovementService.getStockMovementById(stockMovementId);
            return ResponseEntity.ok(stockMovementResponse);

        } catch (RuntimeException e) {
            StockMovementResponse errorResponse = new StockMovementResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            StockMovementResponse errorResponse = new StockMovementResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PostMapping("/prescription-item")
    public ResponseEntity<CreateStockMovementPrescriptionItemResponse> createStockMovementPrescriptionItem(@RequestBody CreateStockMovementPrescriptionItemRequest request){
        try{
            CreateStockMovementPrescriptionItemResponse response = stockMovementService.createStockMovementPrescriptionItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e){
            CreateStockMovementPrescriptionItemResponse errorResponse = new CreateStockMovementPrescriptionItemResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        catch (Exception e){
            CreateStockMovementPrescriptionItemResponse errorResponse = new CreateStockMovementPrescriptionItemResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<CreateStockMovementProductResponse> createStockMovementProduct(@RequestBody CreateStockMovementProductRequest request){
        try{
            CreateStockMovementProductResponse response = stockMovementService.createStockMovementProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e){
            CreateStockMovementProductResponse errorResponse = new CreateStockMovementProductResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        catch (Exception e){
            CreateStockMovementProductResponse errorResponse = new CreateStockMovementProductResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/vaccine")
    public ResponseEntity<CreateStockMovementVaccineResponse> createStockMovementVaccine(@RequestBody CreateStockMovementVaccineRequest request){
        try{
            CreateStockMovementVaccineResponse response = stockMovementService.createStockMovementVaccine(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e){
            CreateStockMovementVaccineResponse errorResponse = new CreateStockMovementVaccineResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        catch (Exception e){
            CreateStockMovementVaccineResponse errorResponse = new CreateStockMovementVaccineResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


}
