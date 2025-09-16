package org.example.petcarebe.controller.admin;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.testresult.CreateTestResultRequest;
import org.example.petcarebe.dto.request.testresult.UpdateTestResultRequest;
import org.example.petcarebe.dto.response.testresult.TestResultResponse;
import org.example.petcarebe.dto.response.testresult.TestResultStatisticsResponse;
import org.example.petcarebe.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    /**
     * Create a new test result
     * Only ADMIN and DOCTOR can create test results
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<TestResultResponse> createTestResult(@Valid @RequestBody CreateTestResultRequest request) {
        try {
            TestResultResponse response = testResultService.createTestResult(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Error creating test result: " + e.getMessage());

            TestResultResponse errorResponse = new TestResultResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            TestResultResponse errorResponse = new TestResultResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all test results
     * Accessible by all authenticated users
     */
    @GetMapping
    public ResponseEntity<List<TestResultResponse>> getAllTestResults() {
        List<TestResultResponse> testResults = testResultService.getAllTestResults();
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test result by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestResultResponse> getTestResultById(@PathVariable Long id) {
        try {
            TestResultResponse response = testResultService.getTestResultById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting test result: " + e.getMessage());

            TestResultResponse errorResponse = new TestResultResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Update test result
     * Only ADMIN and DOCTOR can update test results
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<TestResultResponse> updateTestResult(@PathVariable Long id,
                                                             @Valid @RequestBody UpdateTestResultRequest request) {
        try {
            TestResultResponse response = testResultService.updateTestResult(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating test result: " + e.getMessage());

            TestResultResponse errorResponse = new TestResultResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete test result
     * Only ADMIN can delete test results
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TestResultResponse> deleteTestResult(@PathVariable Long id) {
        try {
            TestResultResponse response = testResultService.deleteTestResult(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error deleting test result: " + e.getMessage());

            TestResultResponse errorResponse = new TestResultResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get test results by visit ID
     */
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByVisitId(@PathVariable Long visitId) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByVisitId(visitId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test results by test type
     */
    @GetMapping("/test-type/{testType}")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByTestType(@PathVariable String testType) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByTestType(testType);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test results by pet ID
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByPetId(@PathVariable Long petId) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByPetId(petId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test results by customer ID
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByCustomerId(@PathVariable Long customerId) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByCustomerId(customerId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test results by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByDoctorId(@PathVariable Long doctorId) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByDoctorId(doctorId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test results by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TestResultResponse>> getTestResultsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TestResultResponse> testResults = testResultService.getTestResultsByDateRange(startDate, endDate);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Search test results by result content
     */
    @GetMapping("/search/result")
    public ResponseEntity<List<TestResultResponse>> searchTestResultsByResult(@RequestParam String keyword) {
        List<TestResultResponse> testResults = testResultService.searchTestResultsByResult(keyword);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Search test results by notes
     */
    @GetMapping("/search/notes")
    public ResponseEntity<List<TestResultResponse>> searchTestResultsByNotes(@RequestParam String keyword) {
        List<TestResultResponse> testResults = testResultService.searchTestResultsByNotes(keyword);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Get test result statistics
     * Only ADMIN and DOCTOR can view statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<TestResultStatisticsResponse> getTestResultStatistics() {
        TestResultStatisticsResponse statistics = testResultService.getTestResultStatistics();
        return ResponseEntity.ok(statistics);
    }
}
