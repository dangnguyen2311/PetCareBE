package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.serviceprice.CreateServicePriceHistoryRequest;
import org.example.petcarebe.dto.response.serviceprice.ServicePriceHistoryResponse;
import org.example.petcarebe.service.ServicePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/service-price")
public class ServicePriceHistoryController {
    @Autowired
    private ServicePriceHistoryService servicePriceHistoryService;

    @PostMapping
    public ResponseEntity<ServicePriceHistoryResponse> createPriceHistory(@RequestBody CreateServicePriceHistoryRequest request){
        ServicePriceHistoryResponse response = servicePriceHistoryService.createPriceHistory(request);
        return ResponseEntity.ok(response);
    }
}
