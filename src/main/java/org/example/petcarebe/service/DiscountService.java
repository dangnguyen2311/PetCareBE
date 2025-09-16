package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.discount.CreateDiscountRequest;
import org.example.petcarebe.dto.request.discount.UpdateDiscountRequest;
import org.example.petcarebe.dto.response.discount.DiscountResponse;
import org.example.petcarebe.model.Discount;
import org.example.petcarebe.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public DiscountResponse createDiscount(CreateDiscountRequest request) {
        Discount discount = new Discount();
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setValue(request.getValue());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setMaxAmount(request.getMaxAmount());
        discount.setMinAmount(request.getMinAmount());
        discount.setStatus(request.getStatus());
        discount.setIsDeleted(false);

        Discount savedDiscount = discountRepository.save(discount);
        return convertToResponse(savedDiscount);
    }

    public List<DiscountResponse> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DiscountResponse getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));
        return convertToResponse(discount);
    }

    public DiscountResponse updateDiscount(Long id, UpdateDiscountRequest request) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));

        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setValue(request.getValue());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setMaxAmount(request.getMaxAmount());
        discount.setMinAmount(request.getMinAmount());
        discount.setStatus(request.getStatus());

        Discount updatedDiscount = discountRepository.save(discount);
        return convertToResponse(updatedDiscount);
    }

    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with id: " + id));
        discount.setIsDeleted(true);
        discountRepository.save(discount);
    }

    private DiscountResponse convertToResponse(Discount discount) {
        return new DiscountResponse(
                discount.getId(),
                discount.getCode(),
                discount.getDescription(),
                discount.getValue(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getMaxAmount(),
                discount.getMinAmount(),
                discount.getStatus()
        );
    }
}

