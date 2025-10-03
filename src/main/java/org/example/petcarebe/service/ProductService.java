package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.product.CreateProductRequest;
import org.example.petcarebe.dto.request.product.UpdateProductRequest;
import org.example.petcarebe.dto.response.product.ProductResponse;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.model.InventoryItem;
import org.example.petcarebe.model.InventoryObject;
import org.example.petcarebe.model.Product;
import org.example.petcarebe.repository.InventoryItemRepository;
import org.example.petcarebe.repository.InventoryObjectRepository;
import org.example.petcarebe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryObjectRepository inventoryObjectRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public ProductResponse createProduct(CreateProductRequest request) {
        InventoryObject inventoryObject = new InventoryObject();
        inventoryObject.setName(request.getName());
        inventoryObject.setType(InventoryObjectType.PRODUCT);
        inventoryObject.setDescription(request.getDescription());
        InventoryObject savedInventoryObject = inventoryObjectRepository.save(inventoryObject);

        InventoryItem  inventoryItem = new InventoryItem();
        inventoryItem.setQuantity(0);
        inventoryItem.setName(request.getName());
        inventoryItem.setMinQuantity(0);
        inventoryItem.setUnit(request.getUnit());
        inventoryItem.setCreatedAt(LocalDateTime.now());
        inventoryItem.setUpdatedAt(LocalDateTime.now());
        inventoryItem.setInventoryObject(savedInventoryObject);
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setUnit(request.getUnit());
        product.setSupplier(request.getSupplier());
        product.setIsActive(true);
        product.setImgUrl(request.getImgUrl());
        product.setCreatedDate(LocalDate.now());
        product.setIsDeleted(false);
        product.setInventoryObject(savedInventoryObject);

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllByIsDeleted(false).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setUnit(request.getUnit());
        product.setSupplier(request.getSupplier());
        product.setIsActive(request.getIsActive());
        product.setImgUrl(request.getImgUrl());

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getUnit(),
                product.getSupplier(),
                product.getIsActive(),
                product.getImgUrl(),
                product.getCreatedDate()
        );
    }
}

