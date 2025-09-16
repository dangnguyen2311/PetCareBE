package org.example.petcarebe.dto.response.clinicroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicRoomResponse {
    private Long id;
    private String name;
    private String type;
    private String status;
}

