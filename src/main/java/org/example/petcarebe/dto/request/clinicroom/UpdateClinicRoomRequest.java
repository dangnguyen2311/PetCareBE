package org.example.petcarebe.dto.request.clinicroom;

import lombok.Data;

@Data
public class UpdateClinicRoomRequest {
    private String name;
    private String type;
    private String status;
}

