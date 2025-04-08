package com.example.lessons_service.dto;

import com.example.lessons_service.entity.SchoolSubject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SchoolSubject subject;
    private Long studentId;
    private Long teacherId;
}
