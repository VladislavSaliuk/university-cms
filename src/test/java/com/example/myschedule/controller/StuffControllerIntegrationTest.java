package com.example.myschedule.controller;

import com.example.myschedule.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_classrooms.sql", "/sql/insert_groups.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_lessons.sql"})
public class StuffControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffHomePage_shouldReturnStuffPageView() throws Exception {
        mockMvc.perform(get("/stuff"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-home-page"));
    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffClassroomDashboardPage_shouldReturnStuffClassroomDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/classrooms-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classroomList", "classroomDTO"))
                .andExpect(view().name("stuff-classrooms-dashboard-page"))
                .andReturn();

        List<ClassroomDTO> classroomList = (List<ClassroomDTO>) result.getModelAndView().getModel().get("classroomList");
        ClassroomDTO classroomDTO = (ClassroomDTO) result.getModelAndView().getModel().get("classroomDTO");

        assertEquals(classroomDTO, new ClassroomDTO());

        assertNotNull(classroomList);
        assertFalse(classroomList.isEmpty());
        assertEquals(10, classroomList.size());

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffCourseDashboardPage_shouldReturnStuffCourseDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/courses-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseList" , "teacherList", "courseDTO"))
                .andExpect(view().name("stuff-courses-dashboard-page"))
                .andReturn();

        List<CourseDTO> courseList = (List<CourseDTO>) result.getModelAndView().getModel().get("courseList");
        List<TeacherDTO> teacherList = (List<TeacherDTO>) result.getModelAndView().getModel().get("teacherList");
        CourseDTO courseDTO = (CourseDTO) result.getModelAndView().getModel().get("courseDTO");

        assertEquals(courseDTO, new CourseDTO());

        assertNotNull(courseList);
        assertFalse(courseList.isEmpty());
        assertEquals(10, courseList.size());

        assertNotNull(teacherList);
        assertFalse(teacherList.isEmpty());
        assertEquals(3, teacherList.size());

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffGroupDashboardPage_shouldReturnStuffGroupDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/groups-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groupList", "groupDTO"))
                .andExpect(view().name("stuff-groups-dashboard-page"))
                .andReturn();

        List<GroupDTO> groupList = (List<GroupDTO>) result.getModelAndView().getModel().get("groupList");
        GroupDTO groupDTO = (GroupDTO) result.getModelAndView().getModel().get("groupDTO");

        assertEquals(groupDTO, new GroupDTO());

        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
        assertEquals(10, groupList.size());

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showLessonDashboardPage_shouldReturnLessonDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/lessons-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lessonList","courseList", "groupList", "classroomList", "lessonDTO"))
                .andExpect(view().name("stuff-lessons-dashboard-page"))
                .andReturn();

        List<ClassroomDTO> classroomList = (List<ClassroomDTO>) result.getModelAndView().getModel().get("classroomList");
        List<CourseDTO> courseList = (List<CourseDTO>) result.getModelAndView().getModel().get("courseList");
        List<GroupDTO> groupList = (List<GroupDTO>) result.getModelAndView().getModel().get("groupList");
        List<LessonDTO> lessonList = (List<LessonDTO>) result.getModelAndView().getModel().get("lessonList");
        LessonDTO lessonDTO = (LessonDTO) result.getModelAndView().getModel().get("lessonDTO");

        assertNotNull(classroomList);
        assertFalse(classroomList.isEmpty());
        assertEquals(10, classroomList.size());

        assertNotNull(courseList);
        assertFalse(courseList.isEmpty());
        assertEquals(10, courseList.size());

        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
        assertEquals(10, groupList.size());

        assertNotNull(lessonList);
        assertFalse(lessonList.isEmpty());
        assertEquals(10, lessonList.size());

        assertEquals(lessonDTO, new LessonDTO());

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffStudentDashboardPage_shouldReturnStuffStudentDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/students-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studentList"))
                .andExpect(view().name("stuff-students-dashboard-page"))
                .andReturn();

        List<StudentDTO> studentList = (List<StudentDTO>) result.getModelAndView().getModel().get("studentList");

        assertNotNull(studentList);
        assertFalse(studentList.isEmpty());
        assertEquals(5, studentList.size());

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void showStuffTeacherDashboardPage_shouldReturnStuffTeacherDashboardPageView() throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/teachers-dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teacherList"))
                .andExpect(view().name("stuff-teachers-dashboard-page"))
                .andReturn();

        List<TeacherDTO> teacherList = (List<TeacherDTO>) result.getModelAndView().getModel().get("teacherList");

        assertNotNull(teacherList);
        assertFalse(teacherList.isEmpty());
        assertEquals(3, teacherList.size());

    }

}
