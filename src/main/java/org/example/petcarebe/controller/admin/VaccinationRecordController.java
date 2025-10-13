package org.example.petcarebe.controller.admin;

import org.apache.coyote.Response;
import org.example.petcarebe.dto.request.vaccinationrecord.CreateVaccinationRecordRequest;
import org.example.petcarebe.dto.request.vaccinationrecord.UpdateVaccinationRecordRequest;
import org.example.petcarebe.dto.response.vaccinationrecord.VaccinationRecordResponse;
import org.example.petcarebe.service.VaccinationRecordService;
import org.example.petcarebe.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/vaccine-record")
public class VaccinationRecordController {
    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @PostMapping
    public ResponseEntity<VaccinationRecordResponse> createVaccinationRecord(@RequestBody CreateVaccinationRecordRequest request){
        try{
            VaccinationRecordResponse response = vaccinationRecordService.createVaccinationRecord(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<VaccinationRecordResponse>> getAllVaccinationRecords(){
        try{
            List<VaccinationRecordResponse> responses = vaccinationRecordService.getALlVaccinationRecord();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (RuntimeException e) {
            List<VaccinationRecordResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            List<VaccinationRecordResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccinationRecordResponse> getVaccinationRecordById(@PathVariable Long id){
        try{
            VaccinationRecordResponse response = vaccinationRecordService.getVaccinationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (RuntimeException e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<VaccinationRecordResponse> updateVaccinationRecord(
            @PathVariable Long id,
            @RequestBody UpdateVaccinationRecordRequest request){
        try{
            VaccinationRecordResponse response = vaccinationRecordService.updateVacciantionRecord(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (RuntimeException e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e) {
            VaccinationRecordResponse error = new VaccinationRecordResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

    }
}
