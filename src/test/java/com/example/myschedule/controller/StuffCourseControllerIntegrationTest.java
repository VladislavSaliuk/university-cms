package com.example.myschedule.controller;

import com.example.myschedule.model.Course;
import com.example.myschedule.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StuffCourseControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void showStuffCoursePage_shouldReturnStuffCoursePageView() throws Exception {

        when(courseService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void showAddCoursePage_shouldReturnAddCoursePageView_whenUserIsStuff() throws Exception {

        mockMvc.perform(get("/stuff/courses/add-course"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-course-page"))
                .andExpect(model().attributeExists("course"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void showStuffEditCoursePage_shouldReturnStuffCoursePageView() throws Exception {

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(courseService.getCourseByCourseId(1)).thenReturn(course);

        mockMvc.perform(get("/stuff/courses/edit-course/{courseId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-edit-course-page"))
                .andExpect(model().attributeExists("course"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void createCourse_shouldSaveCourse_whenUserIsStuff() throws Exception {

        doNothing().when(courseService).createCourse(any(Course.class));

        mockMvc.perform(post("/stuff/courses/add-course")
                        .param("courseName", "Test course name")
                        .param("courseDescription", "Test course description"))
                .andExpect(redirectedUrl("/stuff/courses"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void editCourse_shouldUpdateCourse_whenUserIsStuff() throws Exception {

        Course course = new Course();
        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Course description");

        doNothing().when(courseService).updateCourse(course);

        mockMvc.perform(post("/stuff/courses/edit-course/" + course.getCourseId()))
                .andExpect(redirectedUrl("/stuff/courses"));

    }

}
