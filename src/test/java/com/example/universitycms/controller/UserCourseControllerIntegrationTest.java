package com.example.universitycms.controller;


import com.example.universitycms.service.UserCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCourseControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserCourseService userCourseService;

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showAssignTeacherOnCoursePage_shouldReturnStuffAssignTeacherOnCoursePageView() throws Exception {
        long userId = 1;

        when(userCourseService.getUnassignedCoursesForUser(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/teachers/assign-teacher-on-course/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-assign-teacher-on-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showAssignStudentOnCoursePage_shouldReturnStuffAssignStudentOnCoursePageView() throws Exception {
        long userId = 1;

        when(userCourseService.getUnassignedCoursesForUser(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/students/assign-student-on-course/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-assign-student-on-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffRemoveTeacherFromCoursePage_shouldReturnStuffRemoveTeacherFromCoursePageView() throws Exception {

        long userId = 1;

        when(userCourseService.getAssignedCoursesForUser(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/teachers/remove-teacher-from-course/{userid}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-remove-teacher-from-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffRemoveStudentFromCoursePage_shouldReturnStuffRemoveStudentFromCoursePageView() throws Exception {

        long userId = 1;

        when(userCourseService.getAssignedCoursesForUser(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/students/remove-student-from-course/{userid}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-remove-student-from-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void assignTeacherOnCourse_shouldInsertUserCourseToDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).assignUserOnCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/assign-teacher-on-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void assignStudentOnCourse_shouldInsertUserCourseToDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).assignUserOnCourse(userId, courseId);

        mockMvc.perform(post("/stuff/students/assign-student-on-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/students"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void removeTeacherFromCourse_shouldDeleteUserCourseFromDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).removeUserFromCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/remove-teacher-from-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void removeStudentFromCourse_shouldDeleteUserCourseFromDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).removeUserFromCourse(userId, courseId);

        mockMvc.perform(post("/stuff/students/remove-student-from-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/students"));

    }

}
