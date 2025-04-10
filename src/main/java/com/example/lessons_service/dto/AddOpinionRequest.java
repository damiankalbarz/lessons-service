package com.example.lessons_service.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddOpinionRequest {
    @NotBlank
    private String comment;

    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private double rating;
}