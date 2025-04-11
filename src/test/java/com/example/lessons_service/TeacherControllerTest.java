package com.example.lessons_service;

import com.example.lessons_service.controller.TeacherController;
import com.example.lessons_service.dto.AddOpinionRequest;
import com.example.lessons_service.dto.TeacherDTO;
import com.example.lessons_service.service.TeacherService;
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

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    private MockMvc mvc;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private ObjectMapper objectMapper;

    private TeacherDTO teacherDTO;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFirstname("John");
        teacherDTO.setLastname("Doe");
        teacherDTO.setEmail("john.doe@example.com");
        teacherDTO.setPhoneNumber("123456789");
        teacherDTO.setRate(4.5);

        mvc = MockMvcBuilders.standaloneSetup(teacherController)
                .build();
    }

    // Test for creating a new teacher
    @Test
    public void canCreateTeacher() throws Exception {
        given(teacherService.createTeacher(teacherDTO)).willReturn(teacherDTO);

        MockHttpServletResponse response = mvc.perform(
                post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(teacherDTO)
        );
    }

    // Test for getting all teachers
    @Test
    public void canRetrieveAllTeachers() throws Exception {
        given(teacherService.getAllTeachers()).willReturn(java.util.Arrays.asList(teacherDTO));


        MockHttpServletResponse response = mvc.perform(
                get("/api/teachers")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(java.util.Arrays.asList(teacherDTO))
        );
    }

    // Test for retrieving a teacher by ID
    @Test
    public void canRetrieveTeacherById() throws Exception {
        given(teacherService.getTeacherById(1L)).willReturn(teacherDTO);

        MockHttpServletResponse response = mvc.perform(
                get("/api/teachers/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(teacherDTO)
        );
    }

    // Test for updating an existing teacher
    @Test
    public void canUpdateTeacher() throws Exception {
        given(teacherService.updateTeacher(1L, teacherDTO)).willReturn(teacherDTO);

        MockHttpServletResponse response = mvc.perform(
                put("/api/teachers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(teacherDTO)
        );
    }

    // Test for deleting a teacher
    @Test
    public void canDeleteTeacher() throws Exception {
        doNothing().when(teacherService).deleteTeacher(1L);

        MockHttpServletResponse response = mvc.perform(
                delete("/api/teachers/{id}", 1L)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    // Test for adding an opinion to a teacher
    @Test
    public void canAddOpinionToTeacher() throws Exception {
        AddOpinionRequest opinionRequest = new AddOpinionRequest();
        opinionRequest.setComment("Great teacher!");
        opinionRequest.setRating(4.5);

        given(teacherService.addOpinion(1L, opinionRequest.getComment(), opinionRequest.getRating()))
                .willReturn(teacherDTO);


        MockHttpServletResponse response = mvc.perform(
                post("/api/teachers/{id}/opinions", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opinionRequest))
        ).andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                objectMapper.writeValueAsString(teacherDTO)
        );
    }

    // Test for handling invalid teacher data during creation
    @Test
    public void cannotCreateTeacherWithInvalidData() throws Exception {
        teacherDTO.setEmail(""); // invalid email

        MockHttpServletResponse response = mvc.perform(
                post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Test for handling teacher not found when retrieving by ID
    @Test
    public void cannotRetrieveTeacherWhenNotFound() throws Exception {
        given(teacherService.getTeacherById(999L)).willReturn(null);

        MockHttpServletResponse response = mvc.perform(
                get("/api/teachers/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    // Test for handling invalid rating while adding opinion
    @Test
    public void cannotAddOpinionWithInvalidRating() throws Exception {
        AddOpinionRequest opinionRequest = new AddOpinionRequest();
        opinionRequest.setComment("Great teacher!");
        opinionRequest.setRating(6.0); // invalid rating

        MockHttpServletResponse response = mvc.perform(
                post("/api/teachers/{id}/opinions", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opinionRequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
