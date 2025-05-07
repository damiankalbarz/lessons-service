package com.example.lessons_service.dto;

import com.example.lessons_service.entity.SchoolSubject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonPriceDTO {
    private SchoolSubject subject;
    private Integer durationInMinutes;
    private Double price;
}
