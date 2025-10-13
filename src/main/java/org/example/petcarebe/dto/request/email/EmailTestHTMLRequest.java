package org.example.petcarebe.dto.request.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTestHTMLRequest {
    private String to;
    private String subject;
    private String htmlContent;
}
