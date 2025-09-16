package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.productprice.CreateProductPriceHistoryRequest;
import org.example.petcarebe.dto.response.productprice.ProductPriceHistoryResponse;
import org.example.petcarebe.model.Product;
import org.example.petcarebe.model.ProductPriceHistory;
import org.example.petcarebe.repository.ProductPriceHistoryRepository;
import org.example.petcarebe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductPriceHistoryService {
    @Autowired
    private ProductPriceHistoryRepository  productPriceHistoryRepository;

    @Autowired
    private ProductRepository productRepository;


    public ProductPriceHistoryResponse createPriceHistory(CreateProductPriceHistoryRequest request) {
        productPriceHistoryRepository.deactivateAllActiveRecord(LocalDate.now());

        ProductPriceHistory productPriceHistory = new ProductPriceHistory();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()  -> new RuntimeException("Product not found!"));
        productPriceHistory.setProduct(product);
        productPriceHistory.setPrice(request.getPrice());
        productPriceHistory.setStartDate(LocalDate.now());
        productPriceHistory.setEndDate(null);
        productPriceHistory.setStatus("ACTIVE");
        ProductPriceHistory savedProductPriceHistory = productPriceHistoryRepository.save(productPriceHistory);

        return convertToResponse(savedProductPriceHistory);


    }

    private ProductPriceHistoryResponse convertToResponse(ProductPriceHistory productPriceHistory) {
        return ProductPriceHistoryResponse.builder()
                .id(productPriceHistory.getId())
                .productId(productPriceHistory.getProduct().getId())
                .productName(productPriceHistory.getProduct().getName())
                .price(productPriceHistory.getPrice())
                .startDate(productPriceHistory.getStartDate())
                .endDate(productPriceHistory.getEndDate())
                .status(productPriceHistory.getStatus())
                .message("")
                .build();
    }
    private ProductPriceHistoryResponse convertToResponse(ProductPriceHistory productPriceHistory, String message) {
        return ProductPriceHistoryResponse.builder()
                .id(productPriceHistory.getId())
                .productId(productPriceHistory.getProduct().getId())
                .productName(productPriceHistory.getProduct().getName())
                .price(productPriceHistory.getPrice())
                .startDate(productPriceHistory.getStartDate())
                .endDate(productPriceHistory.getEndDate())
                .status(productPriceHistory.getStatus())
                .message(message)
                .build();
    }
}
