package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.medicineprice.CreateMedicinePriceRequest;
import org.example.petcarebe.dto.response.medicineprice.MedicinePriceHistoryResponse;
import org.example.petcarebe.service.MedicinePriceHistoryService;
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
@RequestMapping("/api/admin/v1/medicine-price")
public class MedicinePriceHistoryController {
    @Autowired
    MedicinePriceHistoryService  medicinePriceHistoryService;

    @PostMapping
    public ResponseEntity<MedicinePriceHistoryResponse> addMedicinePriceHistory(@RequestBody CreateMedicinePriceRequest request) {
        try {
            MedicinePriceHistoryResponse response = medicinePriceHistoryService.createPriceHistory(request);

            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            MedicinePriceHistoryResponse error = new MedicinePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            MedicinePriceHistoryResponse error = new MedicinePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping
    public ResponseEntity<List<MedicinePriceHistoryResponse>> getAllMedicinePriceHistory() {
        try{
            List<MedicinePriceHistoryResponse> responses = medicinePriceHistoryService.getAllMedicinePriceHistory();
            return ResponseEntity.ok(responses);
        }
        catch (RuntimeException e) {
            List<MedicinePriceHistoryResponse> responses = new ArrayList<>();
            return ResponseEntity.internalServerError().body(responses);
        }
        catch (Exception e) {
            List<MedicinePriceHistoryResponse> responses = new ArrayList<>();
            return ResponseEntity.badRequest().body(responses);
        }
    }
}
