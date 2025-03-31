package com.example.lessons_service.entity;

enum LessonDuration {
    FORTY_FIVE(45),
    SIXTY(60),
    NINETY(90);

    private final int minutes;

    LessonDuration(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
}

