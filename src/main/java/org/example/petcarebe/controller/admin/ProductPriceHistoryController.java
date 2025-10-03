package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.productprice.CreateProductPriceHistoryRequest;
import org.example.petcarebe.dto.response.productprice.ProductPriceHistoryResponse;
import org.example.petcarebe.service.ProductPriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/product-price")
public class ProductPriceHistoryController {
    @Autowired
    private ProductPriceHistoryService productPriceHistoryService;

    @PostMapping("/add-price")
    public ResponseEntity<ProductPriceHistoryResponse> addProductPriceHistory(@RequestBody CreateProductPriceHistoryRequest request){
        try {
            ProductPriceHistoryResponse response = productPriceHistoryService.createPriceHistory(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ProductPriceHistoryResponse errorResponse = new ProductPriceHistoryResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
        catch (Exception e) {
            ProductPriceHistoryResponse errorResponse = new ProductPriceHistoryResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @GetMapping
    public ResponseEntity<List<ProductPriceHistoryResponse>> getAllProductPriceHistory(){
        try{
            List<ProductPriceHistoryResponse> responses = productPriceHistoryService.getAllProductPriceHistory();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            List<ProductPriceHistoryResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.internalServerError().body(errorResponse);
        } catch (Exception e) {
            List<ProductPriceHistoryResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
