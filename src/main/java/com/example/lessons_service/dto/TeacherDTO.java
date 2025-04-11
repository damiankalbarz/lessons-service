package com.example.lessons_service.dto;

import com.example.lessons_service.entity.Opinion;
import com.example.lessons_service.entity.SchoolSubject;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class TeacherDTO {
    private Long id;
    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstname;

    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastname;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Getter
    @NotBlank(message = "Phone number must not be blank")
    @Size(min = 9, max = 9, message = "Phone number must be 9")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private List<SchoolSubject> subjects;

    @DecimalMin(value = "0.0", message = "Rate must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rate cannot be greater than 5.0")
    private Double rate;

    private List<Opinion> opinions;
}
