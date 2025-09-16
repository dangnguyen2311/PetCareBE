package org.example.petcarebe.dto.request.product;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private String category;
    private String brand;
    private String unit;
    private String supplier;
    private String imgUrl;
}

