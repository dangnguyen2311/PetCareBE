package org.example.petcarebe.controller;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.customer.CreateCustomerRequest;
import org.example.petcarebe.dto.request.customer.UpdateCustomerRequest;
import org.example.petcarebe.dto.response.appointment.AppointmentResponse;
import org.example.petcarebe.dto.response.customer.CustomerRespone;
import org.example.petcarebe.dto.response.customer.CustomerStatisticsResponse;
import org.example.petcarebe.dto.response.pet.PetResponse;
import org.example.petcarebe.model.Appointment;
import org.example.petcarebe.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerRespone> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerRespone createdCustomer = customerService.createCustomer(request);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerRespone>> getAllCustomers() {
        List<CustomerRespone> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerRespone> getCustomerById(@PathVariable Long customerId) {
        CustomerRespone customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerRespone> updateCustomer(@PathVariable Long customerId, @RequestBody UpdateCustomerRequest request) {
        CustomerRespone updatedCustomer = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

//    GET /api/customers/{id}/pets → Lấy danh sách thú cưng của khách hàng đó
//    GET /api/customers/{id}/appointments → Lấy danh sách lịch hẹn của khách hàng
//    GET /api/customers/{id}/invoices → Lấy danh sách hóa đơn của khách hàng
//    GET /api/customers/{id}/medical-records → Lấy hồ sơ bệnh án của thú cưng thuộc khách hàng
//    GET /api/customers/search?phone=...&email=... → Tìm kiếm khách hàng theo số điện thoại hoặc email
//    PATCH /api/customers/{id}/status → Đổi trạng thái (ACTIVE → INACTIVE hoặc ngược lại)
//    GET /api/customers/statistics/active → Số lượng khách hàng đang hoạt động
//    GET /api/customers/statistics/new?from=2025-01-01&to=2025-03-01 → Số khách hàng đăng ký mới trong khoảng thời gian
//    GET /api/customers/{id}/statistics → Thống kê chi tiêu, số lần đặt lịch, số thú cưng … của 1 khách hàng

    @GetMapping("/{customerId}/pets")
    public ResponseEntity<List<PetResponse>> getPetsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(new ArrayList<>());

    }

    @GetMapping("/{customerId}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByCustomerId(@PathVariable(name = "customerId") Long customerId){
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{customerId}/invoice")
    public ResponseEntity<List<AppointmentResponse>> getInvoicesByCustomerId(@PathVariable(name = "customerId") Long customerId){
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{customerId}/medical-records")
    public ResponseEntity<List<AppointmentResponse>> getMedicalRecordsByCustomerId(@PathVariable(name = "customerId") Long customerId){
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerRespone>> searchCustomers(
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "email", required = false) String email) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    // PATCH /api/customers/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> toggleCustomerStatus(@PathVariable(name = "id") Long customerId) {
        // TODO: implement toggle logic
        return ResponseEntity.ok().build();
    }

    // GET /api/customers/statistics/active
    @GetMapping("/statistics/active")
    public ResponseEntity<Long> getActiveCustomerCount() {
        // TODO: implement count logic
        return ResponseEntity.ok(0L);
    }

    // GET /api/customers/statistics/new?from=2025-01-01&to=2025-03-01
    @GetMapping("/statistics/new")
    public ResponseEntity<Long> getNewCustomerCount(
            @RequestParam(name = "from") String fromDate,
            @RequestParam(name = "to") String toDate) {
        // TODO: implement range filter
        return ResponseEntity.ok(0L);
    }

    // GET /api/customers/{id}/statistics
    @GetMapping("/{id}/statistics")
    public ResponseEntity<CustomerStatisticsResponse> getCustomerStatistics(
            @PathVariable(name = "id") Long customerId) {
        // TODO: implement statistics response
        return ResponseEntity.ok(new CustomerStatisticsResponse());
    }
}

