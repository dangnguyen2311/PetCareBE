package org.example.petcarebe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petcarebe.controller.admin.PrescriptionController;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionItemRequest;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionRequest;
import org.example.petcarebe.dto.request.prescription.UpdatePrescriptionItemRequest;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private PrescriptionResponse samplePrescriptionResponse;
    private PrescriptionItemResponse sampleItemResponse;
    private CreatePrescriptionItemRequest createItemRequest;
    private UpdatePrescriptionItemRequest updateItemRequest;

    @BeforeEach
    void setUp() {
        sampleItemResponse = PrescriptionItemResponse.builder()
                .id(1L)
                .dosage("2 tablets")
                .duration("7 days")
                .instruction("Take after meals")
                .price(100.0)
                .taxPercent(10.0)
                .promotionAmount(5.0)
                .totalAmount(105.0)
                .medicineId(1L)
                .medicineName("Paracetamol")
                .medicineDescription("Pain reliever")
                .prescriptionId(1L)
                .build();

        samplePrescriptionResponse = PrescriptionResponse.builder()
                .id(1L)
                .createdDate(LocalDate.now())
                .note("Test prescription")
                .invoiceId(1L)
                .items(Arrays.asList(sampleItemResponse))
                .totalAmount(105.0)
                .itemCount(1)
                .build();

        createItemRequest = CreatePrescriptionItemRequest.builder()
                .medicineId(1L)
                .dosage("2 tablets")
                .duration("7 days")
                .instruction("Take after meals")
                .taxPercent(10.0)
                .build();

        updateItemRequest = UpdatePrescriptionItemRequest.builder()
                .medicineId(1L)
                .dosage("3 tablets")
                .duration("10 days")
                .instruction("Take before meals")
                .taxPercent(10.0)
                .build();
    }

    @Test
    void testCreatePrescription() throws Exception {
        CreatePrescriptionRequest request = CreatePrescriptionRequest.builder()
                .notes("Test prescription")
                .invoiceId(1L)
                .build();

        when(prescriptionService.createPrescription(any(CreatePrescriptionRequest.class)))
                .thenReturn(samplePrescriptionResponse);

        mockMvc.perform(post("/api/admin/v1/prescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.note").value("Test prescription"));
    }

    @Test
    void testGetPrescriptionById() throws Exception {
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(samplePrescriptionResponse);

        mockMvc.perform(get("/api/admin/v1/prescription/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(1L));
    }

    @Test
    void testAddPrescriptionItem() throws Exception {
        when(prescriptionService.addPrescriptionItem(eq(1L), any(CreatePrescriptionItemRequest.class)))
                .thenReturn(sampleItemResponse);

        mockMvc.perform(post("/api/admin/v1/prescription/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dosage").value("2 tablets"))
                .andExpect(jsonPath("$.medicineId").value(1L));
    }

    @Test
    void testGetPrescriptionItems() throws Exception {
        List<PrescriptionItemResponse> items = Arrays.asList(sampleItemResponse);
        when(prescriptionService.getPrescriptionItems(1L)).thenReturn(items);

        mockMvc.perform(get("/api/admin/v1/prescription/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].dosage").value("2 tablets"));
    }

    @Test
    void testGetPrescriptionItemById() throws Exception {
        when(prescriptionService.getPrescriptionItemById(1L)).thenReturn(sampleItemResponse);

        mockMvc.perform(get("/api/admin/v1/prescription/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dosage").value("2 tablets"));
    }

    @Test
    void testUpdatePrescriptionItem() throws Exception {
        PrescriptionItemResponse updatedResponse = PrescriptionItemResponse.builder()
                .id(1L)
                .dosage("3 tablets")
                .duration("10 days")
                .instruction("Take before meals")
                .price(150.0)
                .taxPercent(10.0)
                .promotionAmount(10.0)
                .totalAmount(155.0)
                .medicineId(1L)
                .medicineName("Paracetamol")
                .medicineDescription("Pain reliever")
                .prescriptionId(1L)
                .build();

        when(prescriptionService.updatePrescriptionItem(eq(1L), any(UpdatePrescriptionItemRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/admin/v1/prescription/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dosage").value("3 tablets"))
                .andExpect(jsonPath("$.duration").value("10 days"));
    }

    @Test
    void testDeletePrescriptionItem() throws Exception {
        mockMvc.perform(delete("/api/admin/v1/prescription/items/1"))
                .andExpect(status().isNoContent());
    }
}
