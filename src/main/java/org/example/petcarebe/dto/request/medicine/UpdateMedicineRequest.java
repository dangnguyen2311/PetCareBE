package org.example.petcarebe.dto.request.medicine;

import lombok.Data;

@Data
public class UpdateMedicineRequest {
    private String name;
    private String description;
    private String unit;
    private String notes;
}
