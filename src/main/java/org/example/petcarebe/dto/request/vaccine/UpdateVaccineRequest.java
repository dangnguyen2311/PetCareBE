package org.example.petcarebe.dto.request.vaccine;

import lombok.Data;

@Data
public class UpdateVaccineRequest {
    private String name;
    private String manufacturer;
    private String description;
}

