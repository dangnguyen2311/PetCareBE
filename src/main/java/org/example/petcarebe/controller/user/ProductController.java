package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.product.CreateProductRequest;
import org.example.petcarebe.dto.request.product.UpdateProductRequest;
import org.example.petcarebe.dto.response.product.ProductResponse;
import org.example.petcarebe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        try {
            ProductResponse createdProduct = productService.createProduct(request);
            return ResponseEntity.ok(createdProduct);
        } catch (RuntimeException e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.badRequest().body(errorProduct);
        }
        catch (Exception e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorProduct);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            List<ProductResponse> errorProducts = new ArrayList<>();
            return ResponseEntity.badRequest().body(errorProducts);
        }
        catch (Exception e) {
            List<ProductResponse> errorProducts = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorProducts);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            ProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.badRequest().body(errorProduct);
        }
        catch (Exception e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorProduct);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.badRequest().body(errorProduct);
        }
        catch (Exception e) {
            ProductResponse errorProduct = new ProductResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorProduct);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
        catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

