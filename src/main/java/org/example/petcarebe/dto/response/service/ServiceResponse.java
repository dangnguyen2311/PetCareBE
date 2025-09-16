package org.example.petcarebe.dto.response.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private String imgUrl;
    private String status;
    private Integer duration;
    private String category;
    private LocalDate createdDate;
    private LocalDate updatedAt;
}

