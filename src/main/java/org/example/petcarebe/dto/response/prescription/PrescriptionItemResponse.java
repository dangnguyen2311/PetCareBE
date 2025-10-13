package org.example.petcarebe.dto.response.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionItemResponse {
    private Long id;
    private String dosage;
    private String duration;
    private Integer quantity;
    private String instruction;
    private Double price;
    private Double taxPercent;
    private Double promotionAmount;
    private Double totalAmount; // Calculated field: price + (price * taxPercent/100) - promotionAmount
    
    // Medicine information
    private Long medicineId;
    private String medicineName;
    private String medicineDescription;
    
    // Prescription information
    private Long prescriptionId;
    private String message;
}
