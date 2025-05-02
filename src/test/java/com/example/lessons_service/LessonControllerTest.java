package com.example.lessons_service;

import com.example.lessons_service.controller.LessonController;
import com.example.lessons_service.dto.LessonDto;
import com.example.lessons_service.entity.SchoolSubject;
import com.example.lessons_service.service.LessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class LessonControllerTest {

    private MockMvc mvc;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private ObjectMapper objectMapper;

    private LessonDto lessonDto;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        lessonDto = new LessonDto();
        lessonDto.setId(1L);
        lessonDto.setStartTime(LocalDateTime.of(2025, 5, 2, 12, 52, 34, 419_714_000));
        lessonDto.setEndTime(LocalDateTime.of(2025, 5, 2, 13, 52, 34, 419_714_000));
        lessonDto.setStudentId(100L);
        lessonDto.setTeacherId(200L);
        lessonDto.setSubject(SchoolSubject.CHEMISTRY);

        mvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    public void canGetAllLessons() throws Exception {
        given(lessonService.getAllLessons()).willReturn(List.of(lessonDto));

        MockHttpServletResponse response = mvc.perform(
                get("/api/lessons")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"id\":1");
    }

    @Test
    public void canCreateLesson() throws Exception {
        given(lessonService.createLesson(lessonDto)).willReturn(lessonDto);

        MockHttpServletResponse response = mvc.perform(
                post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto))
        ).andReturn().getResponse();

        LessonDto actual = objectMapper.readValue(response.getContentAsString(), LessonDto.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison().isEqualTo(lessonDto);
    }


    @Test
    public void canUpdateLesson() throws Exception {
        given(lessonService.updateLesson(1L, lessonDto)).willReturn(lessonDto);

        MockHttpServletResponse response = mvc.perform(
                put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto))
        ).andReturn().getResponse();

        LessonDto actual = objectMapper.readValue(response.getContentAsString(), LessonDto.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison().isEqualTo(lessonDto);
    }

    @Test
    public void canDeleteLesson() throws Exception {
        doNothing().when(lessonService).deleteLesson(1L);

        MockHttpServletResponse response = mvc.perform(
                delete("/api/lessons/1")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
