package com.example.myschedule.controller;

import com.example.myschedule.model.User;
import com.example.myschedule.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    public void showRegistrationPage_shouldShowRegistrationPageView() throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-page"));

    }

    @Test
    public void showRegistrationPageForStudent_shouldShowRegisteredStudentPageView() throws Exception {
        mockMvc.perform(get("/registration/student"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    public void registerStudent_shouldRegisterStudentAndRedirectToLoginPageView() throws Exception {

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/registration/student")
                        .param("firstName", "Test")
                        .param("lastName", "User")
                        .param("userName", "testUser")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().isOk());

    }

    @Test
    public void showRegistrationPageForTeacher_shouldShowRegisteredTeacherPageView() throws Exception {
        mockMvc.perform(get("/registration/teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    public void registerTeacher_shouldRegisterTeacherAndRedirectToLoginPageView() throws Exception {

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/registration/teacher")
                        .param("firstName", "Test")
                        .param("lastName", "Teacher")
                        .param("userName", "teacherUser")
                        .param("email", "teacher@example.com")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().isOk());

    }



}