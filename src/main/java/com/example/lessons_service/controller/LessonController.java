package com.example.lessons_service.controller;

import com.example.lessons_service.dto.LessonDto;
import com.example.lessons_service.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@Valid @RequestBody LessonDto dto) {
        return ResponseEntity.ok(lessonService.createLesson(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDto dto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}