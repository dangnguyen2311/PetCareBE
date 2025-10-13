package org.example.petcarebe.dto.response.servicepackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePackageItemResponse {
    private Long serviceId;
    private String serviceName;
    private String serviceCategory;
    private String serviceDescription;
}
