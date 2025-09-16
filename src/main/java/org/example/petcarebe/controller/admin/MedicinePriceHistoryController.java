package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.medicineprice.CreateMedicinePriceRequest;
import org.example.petcarebe.dto.response.medicineprice.MedicinePriceHistoryResponse;
import org.example.petcarebe.service.MedicinePriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/medicine-price")
public class MedicinePriceHistoryController {
    @Autowired
    MedicinePriceHistoryService  medicinePriceHistoryService;

    @PostMapping("/add-price")
    public ResponseEntity<MedicinePriceHistoryResponse> addMedicinePriceHistory(@RequestBody CreateMedicinePriceRequest request) {
        MedicinePriceHistoryResponse response = medicinePriceHistoryService.createPriceHistory(request);

        return ResponseEntity.ok(response);
    }
}
