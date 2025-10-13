package org.example.petcarebe.dto.response.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTestHTMLResponse {
    private boolean success;
    private String message;
}
