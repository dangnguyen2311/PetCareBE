package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.vaccineprice.CreateVaccinePriceHistoryRequest;
import org.example.petcarebe.dto.response.vaccineprice.VaccinePriceHistoryResponse;
import org.example.petcarebe.service.VaccinePriceHistoryService;
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
@RequestMapping("/api/admin/v1/vaccine-price")
public class VaccinePriceHistoryController {

    @Autowired
    private VaccinePriceHistoryService  vaccinePriceHistoryService;

    @PostMapping
    public ResponseEntity<VaccinePriceHistoryResponse> createPriceHistory(@RequestBody CreateVaccinePriceHistoryRequest request){
        try {
            VaccinePriceHistoryResponse response = vaccinePriceHistoryService.createPriceHistory(request);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            VaccinePriceHistoryResponse error = new VaccinePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            VaccinePriceHistoryResponse error = new VaccinePriceHistoryResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<VaccinePriceHistoryResponse>> getAllVaccinePriceHistory(){
        try{
            List<VaccinePriceHistoryResponse> responses = vaccinePriceHistoryService.getAllVaccinePriceHistory();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            List<VaccinePriceHistoryResponse> responses = new ArrayList<>();
            return ResponseEntity.internalServerError().body(responses);
        }
        catch (Exception e) {
            List<VaccinePriceHistoryResponse> responses = new ArrayList<>();
            return ResponseEntity.badRequest().body(responses);
        }
    }
}
