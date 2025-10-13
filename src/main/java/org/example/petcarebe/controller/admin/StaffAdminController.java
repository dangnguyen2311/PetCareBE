package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.staff.CreateStaffRequest;
import org.example.petcarebe.dto.response.staff.StaffResponse;
import org.example.petcarebe.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/v1/staffs")
public class StaffAdminController {
    @Autowired
    private StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffResponse> createNewStaff(@RequestBody CreateStaffRequest createStaffRequest) {
        try{
            StaffResponse staffResponse = staffService.createStaff(createStaffRequest);
            return ResponseEntity.ok(staffResponse);
        } catch (RuntimeException e) {
            StaffResponse error = new StaffResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            StaffResponse error = new StaffResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffResponse> getMyProfile(Authentication authentication) {
        try{
            String username = authentication.getName();
            StaffResponse response = staffService.getStaffProfile(username);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            StaffResponse error = new StaffResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e){
            StaffResponse error = new StaffResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
