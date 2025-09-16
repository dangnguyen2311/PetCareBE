package org.example.petcarebe.dto.request.service;

import lombok.Data;

@Data
public class CreateServiceRequest {
    private String name;
    private String description;
    private String imgUrl;
    private String status;
    private Integer duration;
    private String category;
}

