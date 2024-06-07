package com.example.universitycms.controller;

import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
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
        mockMvc.perform(get("/registration/registered-student"))
                .andExpect(status().isOk())
                .andExpect(view().name("registered-student-page"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    public void registerStudent_shouldRegisterStudentAndRedirectToLoginPageView() throws Exception {

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/registered-student")
                        .param("username", "testUser")
                        .param("password", "testPassword")
                        .param("otherModelAttribute", "otherModelAttribute"))
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @Test
    public void showRegistrationPageForTeacher_shouldShowRegisteredTeacherPageView() throws Exception {
        mockMvc.perform(get("/registration/registered-teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("registered-teacher-page"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    public void registerTeacher_shouldRegisterTeacherAndRedirectToLoginPageView() throws Exception {

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/registered-teacher")
                        .param("username", "testUser")
                        .param("password", "testPassword")
                        .param("otherModelAttribute", "otherModelAttribute"))
                .andExpect(redirectedUrl("http://localhost/login"));

    }



}
