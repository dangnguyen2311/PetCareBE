package org.example.petcarebe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petcarebe.controller.admin.InventoryObjectController;
import org.example.petcarebe.dto.request.inventoryobject.CreateInventoryObjectRequest;
import org.example.petcarebe.dto.request.inventoryobject.UpdateInventoryObjectRequest;
import org.example.petcarebe.dto.response.inventoryobject.InventoryObjectResponse;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.service.InventoryObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryObjectController.class)
public class InventoryObjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryObjectService inventoryObjectService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryObjectResponse sampleResponse;
    private CreateInventoryObjectRequest createRequest;
    private UpdateInventoryObjectRequest updateRequest;

    @BeforeEach
    void setUp() {
        sampleResponse = new InventoryObjectResponse(
                1L,
                "Test Medicine",
                "Test medicine description",
                InventoryObjectType.MEDICINE,
                "Medicine",
                "Pharmaceutical drugs and medications"
        );

        createRequest = new CreateInventoryObjectRequest();
        createRequest.setName("Test Medicine");
        createRequest.setDescription("Test medicine description");
        createRequest.setType(InventoryObjectType.MEDICINE);

        updateRequest = new UpdateInventoryObjectRequest();
        updateRequest.setName("Updated Medicine");
        updateRequest.setDescription("Updated medicine description");
        updateRequest.setType(InventoryObjectType.MEDICINE);
    }

    @Test
    void testCreateInventoryObject() throws Exception {
        when(inventoryObjectService.createInventoryObject(any(CreateInventoryObjectRequest.class)))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/admin/v1/inventory-objects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Medicine"))
                .andExpect(jsonPath("$.type").value("MEDICINE"));
    }

    @Test
    void testGetAllInventoryObjects() throws Exception {
        List<InventoryObjectResponse> responses = Arrays.asList(sampleResponse);
        when(inventoryObjectService.getAllInventoryObjects()).thenReturn(responses);

        mockMvc.perform(get("/api/admin/v1/inventory-objects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Medicine"));
    }

    @Test
    void testGetInventoryObjectById() throws Exception {
        when(inventoryObjectService.getInventoryObjectById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/admin/v1/inventory-objects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Medicine"));
    }

    @Test
    void testUpdateInventoryObject() throws Exception {
        InventoryObjectResponse updatedResponse = new InventoryObjectResponse(
                1L,
                "Updated Medicine",
                "Updated medicine description",
                InventoryObjectType.MEDICINE,
                "Medicine",
                "Pharmaceutical drugs and medications"
        );

        when(inventoryObjectService.updateInventoryObject(eq(1L), any(UpdateInventoryObjectRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/admin/v1/inventory-objects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Medicine"));
    }

    @Test
    void testDeleteInventoryObject() throws Exception {
        mockMvc.perform(delete("/api/admin/v1/inventory-objects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetInventoryObjectsByType() throws Exception {
        List<InventoryObjectResponse> responses = Arrays.asList(sampleResponse);
        when(inventoryObjectService.getInventoryObjectsByType(InventoryObjectType.MEDICINE))
                .thenReturn(responses);

        mockMvc.perform(get("/api/admin/v1/inventory-objects/type/MEDICINE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("MEDICINE"));
    }

    @Test
    void testSearchInventoryObjectsByName() throws Exception {
        List<InventoryObjectResponse> responses = Arrays.asList(sampleResponse);
        when(inventoryObjectService.searchInventoryObjectsByName("medicine"))
                .thenReturn(responses);

        mockMvc.perform(get("/api/admin/v1/inventory-objects/search")
                        .param("name", "medicine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Medicine"));
    }
}
