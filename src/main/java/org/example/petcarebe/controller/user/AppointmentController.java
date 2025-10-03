package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.appointment.ConfirmAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.CreateAppointmentRequest;
import org.example.petcarebe.dto.request.appointment.UpdateAppointmentRequest;
import org.example.petcarebe.dto.request.visit.CreateVisitRequest;
import org.example.petcarebe.dto.response.appointment.AppointmentResponse;
import org.example.petcarebe.dto.response.appointment.CreateAppointmentResponse;
import org.example.petcarebe.dto.response.visit.CreateVisitResponse;
import org.example.petcarebe.service.AppointmentService;
import org.example.petcarebe.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1/appointments")
public class AppointmentController {
    // Your controller methods will go here
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private VisitService visitService;

    // Create new Appointment
    @PostMapping
    public ResponseEntity<CreateAppointmentResponse> addAppointment(@RequestBody CreateAppointmentRequest request) {
        try {
            CreateAppointmentResponse response = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Log error for debugging
            System.err.println("Error creating appointment: " + e.getMessage());

            CreateAppointmentResponse error = new CreateAppointmentResponse();
            error.setMessage(e.getMessage()); // "Customer not found with id: ..."

            // Return appropriate HTTP status based on error type
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("Unexpected error: " + e.getMessage());

            CreateAppointmentResponse error = new CreateAppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        try {
            List<AppointmentResponse> responses = appointmentService.getAllAppointments();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (RuntimeException e) {
            List<AppointmentResponse> errors = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
        catch (Exception e){
            List<AppointmentResponse> errors = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }

    // Get Appointment
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        try {
            AppointmentResponse response = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting appointment: " + e.getMessage());

            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Get Appointment by ClientId
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentByClientId(@PathVariable String clientId) {
        try {
            List<AppointmentResponse> response = appointmentService.getAppointmentsByClientId(clientId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting appointment: " + e.getMessage());

            List<AppointmentResponse> error = new ArrayList<>();
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            List<AppointmentResponse> error = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //Confirmed
    @PostMapping("/confirm/{appointmentId}")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable Long appointmentId, @RequestBody ConfirmAppointmentRequest request) {
        try{
            AppointmentResponse response = appointmentService.confirmAppointment(appointmentId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Canceled
    @GetMapping("/cancel/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long appointmentId) {
        try{
            AppointmentResponse response = appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Reschedule
    @PostMapping("/reschedule/{id}")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable Long id, @RequestBody UpdateAppointmentRequest request) {
        try{
            AppointmentResponse response = appointmentService.rescheduleAppointment(id, request);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //Update from CONFIRM TO INPROGRESS
    @GetMapping("/to-inprogress/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateInProgressAppointment(@PathVariable Long appointmentId) {
        try{
            AppointmentResponse response = appointmentService.updateToInProgressAppointment(appointmentId);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Create new Visit, will transfer to VisitController
//    @PostMapping("/create-visit/{id}")
//    public ResponseEntity<CreateVisitResponse> createVisit(@PathVariable Long id, @RequestBody CreateVisitRequest request) {
//        try{
//            CreateVisitResponse response = visitService.createVisit(id, request);
////            return new ResponseEntity<>(response, HttpStatus.OK);
//            return ResponseEntity.ok(response);
//        }
//        catch (RuntimeException e) {
//            CreateVisitResponse error = new CreateVisitResponse();
//            error.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//        }
//        catch(Exception e){
//            CreateVisitResponse error = new CreateVisitResponse();
//            error.setMessage("An unexpected error occurred");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
//    }

    // Update to Completed
    @GetMapping("/to-complete/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateCompletedAppointment(@PathVariable Long appointmentId) {
        try{
            AppointmentResponse response = appointmentService.updateToCompleteAppointment(appointmentId);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Update to NoShow
    @GetMapping("/to-noshow/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateNoShowAppointment(@PathVariable Long appointmentId) {
        try{
            AppointmentResponse response = appointmentService.updateToNoShowAppointment(appointmentId);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            AppointmentResponse error = new AppointmentResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}

