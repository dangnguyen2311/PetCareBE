package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.productprice.CreateProductPriceHistoryRequest;
import org.example.petcarebe.dto.response.productprice.ProductPriceHistoryResponse;
import org.example.petcarebe.service.ProductPriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/product-price")
public class ProductPriceHistoryController {
    @Autowired
    private ProductPriceHistoryService productPriceHistoryService;

    @PostMapping("/add-price")
    public ResponseEntity<ProductPriceHistoryResponse> addProductPriceHistory(@RequestBody CreateProductPriceHistoryRequest request){
        ProductPriceHistoryResponse response = productPriceHistoryService.createPriceHistory(request);
        return ResponseEntity.ok(response);
    }
}
