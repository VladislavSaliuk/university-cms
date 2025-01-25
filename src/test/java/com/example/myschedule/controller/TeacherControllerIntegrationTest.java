package com.example.myschedule.controller;

import com.example.myschedule.dto.LessonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(roles = "TEACHER")
    public void showTeacherHomePage_shouldReturnTeacherHomePageView() throws Exception {
        mockMvc.perform(get("/teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-home-page"));
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L, 5L})
    @WithMockUser(roles = "TEACHER")
    public void showTeacherSchedule_shouldReturnTeacherSchedulePageView(long userId) throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/schedule")
                        .sessionAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lessonList"))
                .andExpect(view().name("teacher-schedule-page"))
                .andReturn();

        List<LessonDTO> lessonList = (List<LessonDTO>) result.getModelAndView().getModel().get("lessonList");

        assertNotNull(lessonList);
        assertFalse(lessonList.isEmpty());

    }

    @Test
    @WithMockUser(roles = "TEACHER")
    public void showTeacherSchedule_shouldRedirectToLoginPage_whenUserNotFound() throws Exception {

        long userId = 100L;

        MvcResult result = mockMvc.perform(get("/teacher/schedule")
                        .sessionAttr("userId", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + userId + " Id not found!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "TEACHER")
    public void showTeacherSchedule_shouldRedirectToLoginPage_whenUserIsNotTeacher(long userId) throws Exception {

        MvcResult result = mockMvc.perform(get("/teacher/schedule")
                        .sessionAttr("userId", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + userId + " Id is not a teacher!", errorMessage);

    }

}
