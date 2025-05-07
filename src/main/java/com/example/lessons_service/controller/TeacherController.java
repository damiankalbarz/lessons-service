package com.example.lessons_service.controller;

import com.example.lessons_service.dto.AddOpinionRequest;
import com.example.lessons_service.dto.LessonPriceDTO;
import com.example.lessons_service.dto.TeacherDTO;
import com.example.lessons_service.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO dto) {
        return ResponseEntity.ok(teacherService.createTeacher(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/opinions")
    public ResponseEntity<TeacherDTO> addOpinion(
            @PathVariable Long id,
            @RequestBody @Valid AddOpinionRequest request) {
        return ResponseEntity.ok(teacherService.addOpinion(id, request.getComment(), request.getRating()));
    }

    @GetMapping("/{teacherId}/lesson-prices")
    public List<LessonPriceDTO> getLessonPrices(@PathVariable Long teacherId) {
        return teacherService.getLessonPricesByTeacherId(teacherId);
    }
}
