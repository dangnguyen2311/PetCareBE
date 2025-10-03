package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.discount.CreateDiscountRequest;
import org.example.petcarebe.dto.request.discount.UpdateDiscountRequest;
import org.example.petcarebe.dto.response.discount.DiscountResponse;
import org.example.petcarebe.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody CreateDiscountRequest request) {
        try {
            DiscountResponse createdDiscount = discountService.createDiscount(request);
            return ResponseEntity.ok(createdDiscount);
        } catch (RuntimeException e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorDiscount);
        }
        catch (Exception e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorDiscount);
        }
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponse>> getAllDiscounts() {
        try {
            List<DiscountResponse> discounts = discountService.getAllDiscounts();
            return ResponseEntity.ok(discounts);
        } catch (RuntimeException e) {
            List<DiscountResponse> errorDiscount = new ArrayList<>();
            return ResponseEntity.internalServerError().body(errorDiscount);
        }
        catch (Exception e) {
            List<DiscountResponse> errorDiscount = new ArrayList<>();
            return ResponseEntity.badRequest().body(errorDiscount);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getDiscountById(@PathVariable Long id) {
        try {
            DiscountResponse discount = discountService.getDiscountById(id);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorDiscount);
        }
        catch (Exception e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorDiscount);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponse> updateDiscount(@PathVariable Long id, @RequestBody UpdateDiscountRequest request) {
        try {
            DiscountResponse updatedDiscount = discountService.updateDiscount(id, request);
            return ResponseEntity.ok(updatedDiscount);
        } catch (RuntimeException e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorDiscount);
        }
        catch (Exception e) {
            DiscountResponse errorDiscount = new DiscountResponse();
            errorDiscount.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorDiscount);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}

