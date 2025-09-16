package org.example.petcarebe.dto.request.servicepackage;

import lombok.Data;

@Data
public class CreateServicePackageRequest {
    private String name;
    private String description;
    private String imgUrl;
    private String status;
    private Integer duration;
    private String category;
}

