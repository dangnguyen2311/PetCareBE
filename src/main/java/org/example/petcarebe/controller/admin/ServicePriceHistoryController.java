package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.serviceprice.CreateServicePriceHistoryRequest;
import org.example.petcarebe.dto.response.serviceprice.ServicePriceHistoryResponse;
import org.example.petcarebe.service.ServicePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/service-price")
public class ServicePriceHistoryController {
    @Autowired
    private ServicePriceHistoryService servicePriceHistoryService;

    @PostMapping
    public ResponseEntity<ServicePriceHistoryResponse> createPriceHistory(@RequestBody CreateServicePriceHistoryRequest request){
        try {
            ServicePriceHistoryResponse response = servicePriceHistoryService.createPriceHistory(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServicePriceHistoryResponse error = new ServicePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            ServicePriceHistoryResponse error = new ServicePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicePriceHistoryResponse>> getAllServicePriceHistory(){
        try{
            List<ServicePriceHistoryResponse> responses = servicePriceHistoryService.getAllServicePriceHistory();
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e) {
            List<ServicePriceHistoryResponse> error = new ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            List<ServicePriceHistoryResponse> error = new ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }

    }
}
