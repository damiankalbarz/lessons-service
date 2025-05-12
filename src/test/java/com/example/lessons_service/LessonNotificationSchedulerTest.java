package com.example.lessons_service;

import com.example.lessons_service.auth.user.User;
import com.example.lessons_service.entity.Lesson;
import com.example.lessons_service.entity.SchoolSubject;
import com.example.lessons_service.repository.LessonRepository;
import com.example.lessons_service.service.LessonNotificationScheduler;
import com.example.lessons_service.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LessonNotificationSchedulerTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private LessonNotificationScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendNotificationAndMarkLessonAsSent() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lessonTime = now.plusHours(3);

        User User = new User();
        User.setPhoneNumber("123456789");

        Lesson lesson = new Lesson();
        lesson.setStudent(User);
        lesson.setSubject(SchoolSubject.MATHEMATICS);
        lesson.setStartTime(lessonTime);
        lesson.setNotificationSent(false);

        when(lessonRepository.findByStartTimeBetweenAndNotificationSentFalse(
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
            .thenReturn(List.of(lesson));

        scheduler.sendUpcomingLessonNotifications();

        verify(smsService).sendSms(eq("123456789"), contains("MATHEMATICS"));
        verify(lessonRepository).save(lesson);
        assertThat(lesson.isNotificationSent()).isTrue();
    }

    @Test
    void shouldDoNothingIfNoLessonsFound() {
        when(lessonRepository.findByStartTimeBetweenAndNotificationSentFalse(any(), any()))
            .thenReturn(List.of());

        scheduler.sendUpcomingLessonNotifications();

        verify(smsService, never()).sendSms(anyString(), anyString());
        verify(lessonRepository, never()).save(any());
    }
}
