package com.example.myschedule.controller;

import com.example.myschedule.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentCourseControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;


    @Test
    @WithMockUser(username = "kjones", roles = "STUDENT")
    void showStudentCoursePage_shouldReturnStudentCoursePageView() throws Exception {

        long userId = 1;

        when(userService.getAllCoursesForStudentByUserId(userId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/student/courses")
                .sessionAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("student-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

}
