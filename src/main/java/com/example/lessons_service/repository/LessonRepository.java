package com.example.lessons_service.repository;

import com.example.lessons_service.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByStartTimeBetweenAndNotificationSentFalse(LocalDateTime from, LocalDateTime to);

}
