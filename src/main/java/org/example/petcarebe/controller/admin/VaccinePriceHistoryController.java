package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.vaccineprice.CreateVaccinePriceHistoryRequest;
import org.example.petcarebe.dto.response.vaccineprice.VaccinePriceHistoryResponse;
import org.example.petcarebe.service.VaccinePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/vaccine-price")
public class VaccinePriceHistoryController {

    @Autowired
    private VaccinePriceHistoryService  vaccinePriceHistoryService;

    @PostMapping
    public ResponseEntity<VaccinePriceHistoryResponse> createPriceHistory(@RequestBody CreateVaccinePriceHistoryRequest request){
        VaccinePriceHistoryResponse response = vaccinePriceHistoryService.createPriceHistory(request);

        return ResponseEntity.ok(response);
    }
}
