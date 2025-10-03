package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.servicepackageprice.CreateServicePackagePriceHistoryRequest;
import org.example.petcarebe.dto.response.servicepackageprice.ServicePackagePriceHistoryResponse;
import org.example.petcarebe.service.ServicePackagePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/service-package-price")
public class ServicePackagePriceHistoryController {

    @Autowired
    private ServicePackagePriceHistoryService servicePackagePriceHistoryService;

    @PostMapping
    public ResponseEntity<ServicePackagePriceHistoryResponse> addProductPriceHistory(@RequestBody CreateServicePackagePriceHistoryRequest request){
        try {
            ServicePackagePriceHistoryResponse response = servicePackagePriceHistoryService.createPriceHistory(request);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServicePackagePriceHistoryResponse error = new ServicePackagePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            ServicePackagePriceHistoryResponse error = new ServicePackagePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping
    public ResponseEntity<List<ServicePackagePriceHistoryResponse>> getAllServicePackagePriceHistories(){
        try{
            List<ServicePackagePriceHistoryResponse> responses = servicePackagePriceHistoryService.getAllServicePackagePriceHistories();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            List<ServicePackagePriceHistoryResponse> error = new ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            List<ServicePackagePriceHistoryResponse> error = new ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }

    }
}
