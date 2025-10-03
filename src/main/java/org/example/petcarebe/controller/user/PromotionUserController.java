package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.promotion.CreatePromotionRequest;
import org.example.petcarebe.dto.request.promotion.UpdatePromotionRequest;
import org.example.petcarebe.dto.response.promotion.PromotionResponse;
import org.example.petcarebe.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user/v1/promotions")
public class PromotionUserController {
    @Autowired
    private PromotionService promotionService;


    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        List<PromotionResponse> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable Long id) {
        PromotionResponse promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }


}
