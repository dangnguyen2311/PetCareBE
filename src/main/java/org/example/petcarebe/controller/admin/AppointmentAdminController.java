package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.appointment.GetAppointmentByStatusRequest;
import org.example.petcarebe.dto.response.appointment.AppointmentResponse;
import org.example.petcarebe.dto.response.appointment.CreateAppointmentResponse;
import org.example.petcarebe.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/admin/v1/appointments")
public class AppointmentAdminController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/getlist")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentList(@RequestBody GetAppointmentByStatusRequest request){
        try {
            List<AppointmentResponse> response = appointmentService.getAppointmentByStatus(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Log error for debugging
            System.err.println("Error creating appointment: " + e.getMessage());

            List<AppointmentResponse> error = new ArrayList<>();

            // Return appropriate HTTP status based on error type
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("Unexpected error: " + e.getMessage());

            List<AppointmentResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



}
