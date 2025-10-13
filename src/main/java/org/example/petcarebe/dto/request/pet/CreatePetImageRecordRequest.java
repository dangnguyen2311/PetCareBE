package org.example.petcarebe.dto.request.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePetImageRecordRequest {
    private LocalDate recordDate;
    private MultipartFile image;
    private String notes;
}
