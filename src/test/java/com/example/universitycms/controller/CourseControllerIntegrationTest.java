package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
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
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminCoursePage_shouldReturnAdminCoursePageView() throws Exception {

        when(courseService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAddCoursePage_shouldReturnAddCoursePageView() throws Exception {

        mockMvc.perform(get("/admin/courses/add-course"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-course-page"))
                .andExpect(model().attributeExists("course"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showEditCoursePage_shouldReturnCoursePageView() throws Exception {

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(courseService.getCourseByCourseId(1)).thenReturn(course);

        mockMvc.perform(get("/admin/courses/edit-course/{courseId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-course-page"))
                .andExpect(model().attributeExists("course"));

    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createCourse_shouldSaveCourse() throws Exception {

        doNothing().when(courseService).createCourse(any(Course.class));

        mockMvc.perform(post("/admin/courses/add-course")
                        .param("courseName", "Test course name")
                        .param("courseDescription", "Test course description"))
                .andExpect(redirectedUrl("/admin/courses"));

    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteCourse_shouldRemoveCourse() throws Exception {

        Course course = new Course();
        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Course description");

        doNothing().when(courseService).removeCourseByCourseId(course.getCourseId());

        mockMvc.perform(get("/admin/courses/delete-course/" + course.getCourseId()))
                .andExpect(redirectedUrl("/admin/courses"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void editCourse_shouldUpdateCourse() throws Exception {

        Course course = new Course();
        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Course description");

        doNothing().when(courseService).updateCourse(course);

        mockMvc.perform(post("/admin/courses/edit-course/" + course.getCourseId()))
                .andExpect(redirectedUrl("/admin/courses"));

    }
}
