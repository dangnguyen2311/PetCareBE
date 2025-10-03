package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.response.statistic.DoctorDashboardResponse;
import org.example.petcarebe.dto.response.statistic.StaffDashboardResponse;
import org.example.petcarebe.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/doctor/dashboard")
    public ResponseEntity<DoctorDashboardResponse> getDoctorDashboard() {
        try{
            DoctorDashboardResponse response = statisticService.getDoctorDashboard();
            return new ResponseEntity<>(response, HttpStatus.OK);
//            return ResponseEntity.ok(response);
//            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (RuntimeException e) {
            DoctorDashboardResponse error = new DoctorDashboardResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            DoctorDashboardResponse error = new DoctorDashboardResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/staff/dashboard")
    public ResponseEntity<StaffDashboardResponse> getStaffDashboardResponse(){
        try{
            StaffDashboardResponse response = statisticService.getStaffDashboard();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (RuntimeException e){
            StaffDashboardResponse error = new StaffDashboardResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        catch (Exception e){
            StaffDashboardResponse error = new StaffDashboardResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }



}
