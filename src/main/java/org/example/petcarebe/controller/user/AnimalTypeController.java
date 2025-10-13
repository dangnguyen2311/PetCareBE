package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.animaltype.UpdateAnimalTypeRequest;
import org.example.petcarebe.dto.request.animaltype.CreateAnimalTypeRequest;
import org.example.petcarebe.dto.response.animaltype.AnimalTypeResponse;
import org.example.petcarebe.service.AnimalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/animal-types")
public class AnimalTypeController {

    @Autowired
    private AnimalTypeService animalTypeService;

    @PostMapping
    public ResponseEntity<AnimalTypeResponse> createAnimalType(@RequestBody CreateAnimalTypeRequest request) {
        return ResponseEntity.ok(animalTypeService.createAnimalType(request));
    }

    @GetMapping()
    public ResponseEntity<List<AnimalTypeResponse>> getAnimalTypeList() {
        return ResponseEntity.ok(animalTypeService.getAnimalTypes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalTypeResponse> updateAnimalType(@PathVariable Long id, @RequestBody UpdateAnimalTypeRequest request) {
        AnimalTypeResponse updatedAnimalType = animalTypeService.updateAnimalType(id, request);
        return ResponseEntity.ok(updatedAnimalType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AnimalTypeResponse> deleteAnimalType(@PathVariable Long id) {
        AnimalTypeResponse deleteAnimalType = animalTypeService.deleteAnimalType(id);
        return  ResponseEntity.ok(deleteAnimalType);
    }
}

