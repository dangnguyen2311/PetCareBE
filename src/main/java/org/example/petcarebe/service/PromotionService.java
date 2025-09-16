package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.promotion.CreatePromotionRequest;
import org.example.petcarebe.dto.request.promotion.UpdatePromotionRequest;
import org.example.petcarebe.dto.response.promotion.PromotionResponse;
import org.example.petcarebe.model.Promotion;
import org.example.petcarebe.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public PromotionResponse createPromotion(CreatePromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        promotion.setType(request.getType());
        promotion.setValue(request.getValue());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setStatus(request.getStatus());
        promotion.setIsDeleted(false);

        Promotion savedPromotion = promotionRepository.save(promotion);
        return convertToResponse(savedPromotion);
    }

    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
        return convertToResponse(promotion);
    }

    public PromotionResponse updatePromotion(Long id, UpdatePromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));

        promotion.setName(request.getName());
        promotion.setType(request.getType());
        promotion.setValue(request.getValue());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setStatus(request.getStatus());

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return convertToResponse(updatedPromotion);
    }

    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
        promotion.setIsDeleted(true);
        promotionRepository.save(promotion);
    }

    private PromotionResponse convertToResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getType(),
                promotion.getValue(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getStatus()
        );
    }
}

