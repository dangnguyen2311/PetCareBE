package org.example.petcarebe.dto.request.clinicroom;

import lombok.Data;

@Data
public class CreateClinicRoomRequest {
    private String name;
    private String type;
    private String address;
}

