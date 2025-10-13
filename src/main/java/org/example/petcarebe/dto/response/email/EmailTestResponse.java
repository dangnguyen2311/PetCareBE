package org.example.petcarebe.dto.response.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTestResponse {
    private boolean success;   // gửi thành công hay thất bại
    private String message;    // thông báo
    private String[] to;         // email người nhận
    private String subject;    // tiêu đề đã gửi
}
