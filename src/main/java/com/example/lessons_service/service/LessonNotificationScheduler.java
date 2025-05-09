package com.example.lessons_service.service;

import com.example.lessons_service.entity.Lesson;
import com.example.lessons_service.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonNotificationScheduler {

    private final LessonRepository lessonRepository;
    private final SmsService smsService;

    @Scheduled(fixedRate = 60_000) // co minutę
    public void sendUpcomingLessonNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(5);

        List<Lesson> lessons = lessonRepository
            .findByStartTimeBetweenAndNotificationSentFalse(now, targetTime);

        for (Lesson lesson : lessons) {
            String phone = lesson.getStudent().getPhoneNumber();
            String msg = "Przypominowany o zaplanowej lekcji z "+ lesson.getSubject() +" która odbędzie się o " + lesson.getStartTime();

            smsService.sendSms(phone, msg);
            lesson.setNotificationSent(true);
            lessonRepository.save(lesson);
        }
    }
}
