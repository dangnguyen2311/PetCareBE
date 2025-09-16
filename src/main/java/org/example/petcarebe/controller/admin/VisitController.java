package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.visit.CreateVisitRequest;
import org.example.petcarebe.dto.response.visit.CreateVisitResponse;
import org.example.petcarebe.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/v1/visits")
public class VisitController {
    // Your controller methods will go here
    @Autowired
    private VisitService visitService;

    @PostMapping("/create-visit/{id}")
    public ResponseEntity<CreateVisitResponse> createVisit(@PathVariable Long id, @RequestBody CreateVisitRequest request) {
        try{
            CreateVisitResponse response = visitService.createVisit(id, request);
//            return new ResponseEntity<>(response, HttpStatus.OK);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            CreateVisitResponse error = new CreateVisitResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            CreateVisitResponse error = new CreateVisitResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

