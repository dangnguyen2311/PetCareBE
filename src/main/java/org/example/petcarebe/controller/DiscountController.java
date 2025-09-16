package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.discount.CreateDiscountRequest;
import org.example.petcarebe.dto.request.discount.UpdateDiscountRequest;
import org.example.petcarebe.dto.response.discount.DiscountResponse;
import org.example.petcarebe.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody CreateDiscountRequest request) {
        DiscountResponse createdDiscount = discountService.createDiscount(request);
        return ResponseEntity.ok(createdDiscount);
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponse>> getAllDiscounts() {
        List<DiscountResponse> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getDiscountById(@PathVariable Long id) {
        DiscountResponse discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponse> updateDiscount(@PathVariable Long id, @RequestBody UpdateDiscountRequest request) {
        DiscountResponse updatedDiscount = discountService.updateDiscount(id, request);
        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}

