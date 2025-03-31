package com.example.lessons_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Pattern(regexp = "\\d{9}", message = "Phone number must be 9 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    @Enumerated(EnumType.STRING)
    private List<SchoolSubject> subjects;

    @DecimalMin(value = "0.0", message = "Rate must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rate cannot be greater than 5.0")
    private Double rate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opinion> opinions;
}
