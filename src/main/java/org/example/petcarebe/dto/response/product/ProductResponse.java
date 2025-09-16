package org.example.petcarebe.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String brand;
    private String unit;
    private String supplier;
    private Boolean isActive;
    private String imgUrl;
    private LocalDate createdDate;
}

