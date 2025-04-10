package com.example.lessons_service.service;

import com.example.lessons_service.dto.TeacherDTO;
import com.example.lessons_service.entity.Opinion;
import com.example.lessons_service.entity.Teacher;
import com.example.lessons_service.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));
        return mapToDTO(teacher);
    }

    public TeacherDTO createTeacher(TeacherDTO dto) {
        Teacher teacher = mapToEntity(dto);
        return mapToDTO(teacherRepository.save(teacher));
    }

    public TeacherDTO updateTeacher(Long id, TeacherDTO dto) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        teacher.setEmail(dto.getEmail());
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setSubjects(dto.getSubjects());
        teacher.setRate(dto.getRate());
        teacher.setOpinions(dto.getOpinions());

        return mapToDTO(teacherRepository.save(teacher));
    }

    @Transactional
    public TeacherDTO addOpinion(Long teacherId, String comment, double rating) {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Opinion opinion = new Opinion();
        opinion.setContent(comment);
        opinion.setRate(rating);

        if (teacher.getOpinions() == null) {
            teacher.setOpinions(new ArrayList<>());
        }

        teacher.getOpinions().add(opinion);

        double average = teacher.getOpinions().stream()
                .mapToDouble(Opinion::getRate)
                .average()
                .orElse(0.0);
        teacher.setRate(average);

        return mapToDTO(teacherRepository.save(teacher));
    }


    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    private TeacherDTO mapToDTO(Teacher teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setFirstname(teacher.getFirstname());
        dto.setLastname(teacher.getLastname());
        dto.setEmail(teacher.getEmail());
        dto.setPhoneNumber(teacher.getPhoneNumber());
        dto.setSubjects(teacher.getSubjects());
        dto.setRate(teacher.getRate());
        dto.setOpinions(teacher.getOpinions());
        return dto;
    }

    private Teacher mapToEntity(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        teacher.setEmail(dto.getEmail());
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setSubjects(dto.getSubjects());
        teacher.setRate(dto.getRate());
        teacher.setOpinions(dto.getOpinions());
        return teacher;
    }
}