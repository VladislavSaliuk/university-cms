package com.example.myschedule.controller;

import com.example.myschedule.service.UserCourseService;
import com.example.myschedule.service.UserService;
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
public class StuffTeacherControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserCourseService userCourseService;

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffTeacherPage_shouldReturnStuffTeacherPageView_whenUserIsStuff() throws Exception {

        long roleId = 2;

        when(userService.getUsersByRole(roleId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-teacher-page"))
                .andExpect(model().attributeExists("userList"));

    }
    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showAssignTeacherOnCoursePage_shouldReturnStuffAssignTeacherOnCoursePageView() throws Exception {
        long userId = 1;

        when(userCourseService.getUnassignedCoursesForTeacher(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/teachers/assign-teacher-on-course/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-assign-teacher-on-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void removeTeacherFromCourse_shouldDeleteUserCourseFromDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).removeTeacherFromCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/remove-teacher-from-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffRemoveTeacherFromCoursePage_shouldReturnStuffRemoveTeacherFromCoursePageView() throws Exception {

        long userId = 1;

        when(userCourseService.getAssignedCoursesForTeacher(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/teachers/remove-teacher-from-course/{userid}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-remove-teacher-from-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void assignTeacherOnCourse_shouldInsertUserCourseToDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).assignTeacherOnCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/assign-teacher-on-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

    }

}
