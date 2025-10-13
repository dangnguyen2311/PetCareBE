package org.example.petcarebe.dto.request.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTestRequest {
    private String to;        // địa chỉ người nhận
    private String subject;   // tiêu đề
    private String body;      // nội dung
}
