package org.example.petcarebe.dto.request.user;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private Boolean isDeleted;
}

