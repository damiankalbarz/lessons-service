package com.example.lessons_service.dto;

import com.example.lessons_service.entity.Opinion;
import com.example.lessons_service.entity.SchoolSubject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class TeacherDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private List<SchoolSubject> subjects;
    private Double rate;
    private List<Opinion> opinions;
}
