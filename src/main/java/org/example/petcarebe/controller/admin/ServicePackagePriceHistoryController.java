package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.servicepackageprice.CreateServicePackagePriceHistoryRequest;
import org.example.petcarebe.dto.response.servicepackageprice.ServicePackagePriceHistoryResponse;
import org.example.petcarebe.service.ServicePackagePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/service-package-price")
public class ServicePackagePriceHistoryController {

    @Autowired
    private ServicePackagePriceHistoryService servicePackagePriceHistoryService;

    @PostMapping
    public ResponseEntity<ServicePackagePriceHistoryResponse> addProductPriceHistory(@RequestBody CreateServicePackagePriceHistoryRequest request){
        ServicePackagePriceHistoryResponse response = servicePackagePriceHistoryService.createPriceHistory(request);

        return ResponseEntity.ok(response);

    }
}
