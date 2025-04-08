package com.example.lessons_service.service;

import com.example.lessons_service.dto.LessonDto;
import com.example.lessons_service.entity.Lesson;
import com.example.lessons_service.repository.LessonRepository;
import com.example.lessons_service.auth.user.User;
import com.example.lessons_service.auth.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public LessonDto createLesson(LessonDto dto) {
        Lesson lesson = new Lesson();
        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setSubject(dto.getSubject());
        /*
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        lesson.setStudent(student);
        lesson.setTeacher(teacher);
        */
        Lesson saved = lessonRepository.save(lesson);
        return mapToDto(saved);
    }

    @Transactional
    public LessonDto updateLesson(Long id, LessonDto dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setSubject(dto.getSubject());
        /*
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        lesson.setStudent(student);
        lesson.setTeacher(teacher);
        */
        return mapToDto(lessonRepository.save(lesson));
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    private LessonDto mapToDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setStartTime(lesson.getStartTime());
        dto.setEndTime(lesson.getEndTime());
        dto.setSubject(lesson.getSubject());
        //dto.setStudentId(lesson.getStudent().getId());
        //dto.setTeacherId(lesson.getTeacher().getId());
        return dto;
    }
}