package com.example.myschedule.controller;

import com.example.myschedule.dto.*;
import com.example.myschedule.entity.DayOfWeek;
import com.example.myschedule.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    ClassroomDTO classroomDTO;
    GroupDTO groupDTO;
    StudentDTO studentDTO;
    TeacherDTO teacherDTO;
    CourseDTO courseDTO;
    @BeforeEach
    void setUp() {

        classroomDTO = ClassroomDTO.builder()
                .classroomNumber(200L)
                .classroomDescription("Test description")
                .build();

        groupDTO = GroupDTO.builder()
                .groupName("Test group name")
                .build();

        studentDTO = StudentDTO.builder()
                .userId(100L)
                .build();

        teacherDTO = TeacherDTO.builder()
                .userId(3)
                .build();

        courseDTO = CourseDTO.builder()
                .courseId(101L)
                .courseName("Test course name")
                .teacherDTO(teacherDTO)
                .build();

    }
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

    @Test
    @WithMockUser(roles = "STUFF")
    public void createClassroom_shouldReturnSuccessMessage() throws Exception {

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/create")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Classroom created successfully!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createClassroom_shouldReturnErrorMessage_whenClassroomNumberIsNegativ() throws Exception {

        classroomDTO.setClassroomNumber(-123);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/create")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Number cannot be negativ!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {101, 102, 103, 104, 105, 201, 202, 203, 301, 302})
    @WithMockUser(roles = "STUFF")
    public void createClassroom_shouldReturnErrorMessage_whenClassroomNumberRepeats(long classroomNumber) throws Exception {

        classroomDTO.setClassroomNumber(classroomNumber);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/create")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Classroom with " + classroomDTO.getClassroomNumber() + " number already exists!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateClassroom_shouldReturnSuccessMessage(long classroomId) throws Exception {

        classroomDTO.setClassRoomId(classroomId);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/update")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Classroom updated successfully!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateClassroom_shouldReturnErrorMessage_whenClassroomNumberIsNegativ() throws Exception {

        classroomDTO.setClassroomNumber(-123);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/update")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Number cannot be negativ!", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateClassroom_shouldReturnErrorMessage_whenClassroomNotFound() throws Exception {

        classroomDTO.setClassRoomId(100L);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/update")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Classroom with " + classroomDTO.getClassRoomId() + " Id not found!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {102, 103, 104, 105, 201, 202, 203, 301, 302})
    @WithMockUser(roles = "STUFF")
    public void updateClassroom_shouldReturnErrorMessage_whenClassroomNumberRepeats(long classroomNumber) throws Exception {

        classroomDTO.setClassRoomId(1L);
        classroomDTO.setClassroomNumber(classroomNumber);

        MvcResult result = mockMvc.perform(post("/stuff/classrooms-dashboard/update")
                        .flashAttr("classroomDTO", classroomDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Classroom with " + classroomDTO.getClassroomNumber() + " number already exists!", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void removeClassroomById_shouldReturnSuccessMessage() throws Exception {

        classroomDTO.setClassRoomId(100L);

        mockMvc.perform(post("/stuff/classrooms-dashboard/create")
                .param("classroomName", "Test Classroom name"));

        MvcResult result = mockMvc.perform(get("/stuff/classrooms-dashboard/delete?classroomId=" + 11))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Classroom deleted successfully!", successMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void removeClassroomById_shouldReturnErrorMessage_whenClassroomAssociatedWithLesson(long classroomId) throws Exception {


        MvcResult result = mockMvc.perform(get("/stuff/classrooms-dashboard/delete?classroomId=" + classroomId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Cannot delete classroom because it is associated with existing lessons.", errorMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void removeClassroomById_shouldReturnErrorMessage_whenClassroomNotFound() throws Exception {

        long classroomId = 100L;

        MvcResult result = mockMvc.perform(get("/stuff/classrooms-dashboard/delete?classroomId=" + classroomId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/classrooms-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Classroom with " + classroomId + " Id not found!",  errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createGroup_shouldReturnSuccessMessage() throws Exception {

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/create")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Group created successfully!", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void createGroup_shouldReturnErrorMessage_whenGroupNameIsNull() throws Exception {

        groupDTO.setGroupName(null);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/create")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group should contains name!", errorMessage);

    }
    @ParameterizedTest
    @ValueSource(strings = {"TB-24", "MK-23", "MK-24", "FI-23", "FI-24", "CS-23", "CS-24", "BI-23", "BI-24"})
    @WithMockUser(roles = "STUFF")
    public void createGroup_shouldReturnErrorMessage_whenGroupNameRepeats(String groupName) throws Exception {

        groupDTO.setGroupName(groupName);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/create")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group with " + groupDTO.getGroupName() + " name already exists!", errorMessage);

    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateGroup_shouldReturnSuccessMessage(long groupId) throws Exception {

        groupDTO.setGroupId(groupId);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/update")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Group updated successfully!", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void updateGroup_shouldReturnErrorMessage_whenGroupNameIsNull() throws Exception {

        groupDTO.setGroupName(null);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/update")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group should contains name!", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateGroup_shouldReturnErrorMessage_whenGroupNotFound() throws Exception {

        groupDTO.setGroupId(100L);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/update")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group with " + groupDTO.getGroupId() + " Id not found!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(strings = {"TB-24", "MK-23", "MK-24", "FI-23", "FI-24", "CS-23", "CS-24", "BI-23", "BI-24"})
    @WithMockUser(roles = "STUFF")
    public void updateGroup_shouldReturnErrorMessage_whenGroupNameRepeats(String groupName) throws Exception {

        groupDTO.setGroupId(1L);
        groupDTO.setGroupName(groupName);

        MvcResult result = mockMvc.perform(post("/stuff/groups-dashboard/update")
                        .flashAttr("groupDTO", groupDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group with " + groupDTO.getGroupName() + " name already exists!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void removeGroupById_shouldReturnSuccessMessage(long groupId) throws Exception {


        MvcResult result = mockMvc.perform(get("/stuff/groups-dashboard/delete?groupId=" + groupId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(errorMessage);
        assertEquals("Group deleted successfully!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    @WithMockUser(roles = "STUFF")
    public void removeGroupById_shouldReturnErrorMessage_whenGroupAssociatedWithStudents(long groupId) throws Exception {


        MvcResult result = mockMvc.perform(get("/stuff/groups-dashboard/delete?groupId=" + groupId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Cannot delete group because it is associated with existing students.", errorMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void removeGroupById_shouldReturnErrorMessage_whenGroupNotFound() throws Exception {

        long groupId = 100L;

        MvcResult result = mockMvc.perform(get("/stuff/groups-dashboard/delete?groupId=" + groupId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/groups-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Group with " + groupId + " Id not found!",  errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateStudent_shouldReturnSuccessMessage(long groupId) throws Exception {

        groupDTO.setGroupId(groupId);

        studentDTO.setUserId(1L);
        studentDTO.setGroupDTO(groupDTO);

        MvcResult result = mockMvc.perform(post("/stuff/students-dashboard/update")
                        .flashAttr("studentDTO", studentDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/students-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Student updated successfully!", successMessage);

    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateStudent_shouldReturnErrorMessageWhenStudentNotFound(long groupId) throws Exception {

        groupDTO.setGroupId(groupId);

        studentDTO.setUserId(100L);
        studentDTO.setGroupDTO(groupDTO);

        MvcResult result = mockMvc.perform(post("/stuff/students-dashboard/update")
                        .flashAttr("studentDTO", studentDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/students-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Student with " + studentDTO.getUserId() + " Id not found!", successMessage);

    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateStudent_shouldReturnErrorMessageWhenGroupNotFound(long userId) throws Exception {

        groupDTO.setGroupId(100L);

        studentDTO.setUserId(userId);
        studentDTO.setGroupDTO(groupDTO);

        MvcResult result = mockMvc.perform(post("/stuff/students-dashboard/update")
                        .flashAttr("studentDTO", studentDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/students-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Group with " + studentDTO.getGroupDTO().getGroupId() + " Id not found!", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void createCourse_shouldReturnSuccessMessage() throws Exception {

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/create")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Course created successfully!", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void createCourse_shouldReturnErrorMessage_whenCourseNameIsNull() throws Exception {

        courseDTO.setCourseName(null);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/create")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course should contains name!", errorMessage);

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Introduction to Programming",
            "Data Structures and Algorithms",
            "Database Management Systems",
            "Web Development Basics",
            "Advanced Java Programming",
            "Machine Learning Fundamentals",
            "Cybersecurity Essentials",
            "Mobile App Development",
            "Cloud Computing Overview",
            "Software Engineering Principles"
    })
    @WithMockUser(roles = "STUFF")
    public void createCourse_shouldReturnErrorMessage_whenCourseNameRepeats(String courseName) throws Exception {

        courseDTO.setCourseName(courseName);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/create")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course with " + courseDTO.getCourseName() + " name already exists!", errorMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void createCourse_shouldReturnErrorMessage_whenTeacherNotFound() throws Exception {

        courseDTO.getTeacherDTO().setUserId(100L);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/create")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Teacher with " + courseDTO.getTeacherDTO().getUserId() + " Id not found!", errorMessage);

    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void updateCourse_shouldReturnSuccessMessage(long courseId) throws Exception {

        courseDTO.setCourseId(courseId);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/update")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Course updated successfully!", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void updateCourse_shouldReturnErrorMessage_whenCourseNameIsNull() throws Exception {

        courseDTO.setCourseName(null);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/update")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course should contains name!", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateCourse_shouldReturnErrorMessage_whenCourseNotFound() throws Exception {

        courseDTO.setCourseId(100L);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/update")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course with " + courseDTO.getCourseId() + " Id not found!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Data Structures and Algorithms",
            "Database Management Systems",
            "Web Development Basics",
            "Advanced Java Programming",
            "Machine Learning Fundamentals",
            "Cybersecurity Essentials",
            "Mobile App Development",
            "Cloud Computing Overview",
            "Software Engineering Principles"
    })
    @WithMockUser(roles = "STUFF")
    public void updateCourse_shouldReturnErrorMessage_whenCourseNameRepeats(String courseName) throws Exception {

        courseDTO.setCourseId(1L);
        courseDTO.setCourseName(courseName);

        MvcResult result = mockMvc.perform(post("/stuff/courses-dashboard/update")
                        .flashAttr("courseDTO", courseDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course with " + courseDTO.getCourseName() + " name already exists!", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void removeCourseById_shouldReturnSuccessMessage() throws Exception {

        mockMvc.perform(post("/stuff/courses-dashboard/create")
                .param("courseName", "Test course name")
                .param("teacherDTO.userId", "3"));

        MvcResult result = mockMvc.perform(get("/stuff/courses-dashboard/delete?courseId=" + 11))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(errorMessage);
        assertEquals("Course deleted successfully!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void removeCourseById_shouldReturnErrorMessage_whenCourseAssociatedWithLesson(long courseId) throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/courses-dashboard/delete?courseId=" + courseId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Cannot delete course because it is associated with existing lessons.", errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void removeCourseById_shouldReturnErrorMessage_whenCourseNotFound() throws Exception {

        long courseId = 100L;

        MvcResult result = mockMvc.perform(get("/stuff/courses-dashboard/delete?courseId=" + courseId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/courses-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Course with " + courseId + " Id not found!",  errorMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnSuccessMessage() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .courseDTO(courseDTO)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.SATURDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Lesson created successfully!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenStartTimeIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.SATURDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Lesson should contains start time!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenEndTimeIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .dayOfWeek(DayOfWeek.SATURDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Lesson should contains end time!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenDayOfWeekIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Lesson should contains day of week!", successMessage);

    }


    @ParameterizedTest
    @CsvSource({
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenLessonTimesOverlap(String startTime, String endTime) throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.parse(startTime))
                .endTime(LocalTime.parse(endTime))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("The lesson overlaps with an existing lesson.", successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenGroupNotFound() throws Exception {

        groupDTO.setGroupId(100L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Group with " + lessonDTO.getGroupDTO().getGroupId() + " Id not found!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenCourseNotFound() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(100L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Course with " + lessonDTO.getCourseDTO().getCourseId() + " Id not found!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void createLesson_shouldReturnErrorMessage_whenClassroomNotFound() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(100L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
        assertEquals("Classroom with " + lessonDTO.getClassroomDTO().getClassRoomId() + " Id not found!", successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnSuccessMessage() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .courseDTO(courseDTO)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.SATURDAY)
                .build();

        mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andReturn();

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenStartTimeIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenEndTimeIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenDayOfWeekIsNull() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12, 00))
                .endTime(LocalTime.of(13, 00))
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @ParameterizedTest
    @CsvSource({
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenLessonTimesOverlap(String startTime, String endTime) throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.parse(startTime))
                .endTime(LocalTime.parse(endTime))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);
    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenLessonNotFound() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(100L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenGroupNotFound() throws Exception {

        groupDTO.setGroupId(100L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenCourseNotFound() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(100L);
        classroomDTO.setClassRoomId(1L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/update")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @Test
    @WithMockUser(roles = "STUFF")
    public void updateLesson_shouldReturnErrorMessage_whenClassroomNotFound() throws Exception {

        groupDTO.setGroupId(1L);
        courseDTO.setCourseId(1L);
        classroomDTO.setClassRoomId(100L);

        LessonDTO lessonDTO = LessonDTO.builder()
                .lessonId(1L)
                .classroomDTO(classroomDTO)
                .groupDTO(groupDTO)
                .courseDTO(courseDTO)
                .startTime(LocalTime.of(12,00))
                .endTime(LocalTime.of(13,00))
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        MvcResult result = mockMvc.perform(post("/stuff/lessons-dashboard/create")
                        .flashAttr("lessonDTO", lessonDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(successMessage);

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "STUFF")
    public void removeLessonById_shouldReturnSuccessMessage(long lessonId) throws Exception {

        MvcResult result = mockMvc.perform(get("/stuff/lessons-dashboard/delete?lessonId=" + lessonId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(errorMessage);
        assertEquals("Lesson deleted successfully!", errorMessage);

    }
    @Test
    @WithMockUser(roles = "STUFF")
    public void removeLessonById_shouldReturnErrorMessage_whenLessonNotFound() throws Exception {

        long lessonId = 100L;

        MvcResult result = mockMvc.perform(get("/stuff/lessons-dashboard/delete?lessonId=" + lessonId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stuff/lessons-dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("Lesson with " + lessonId + " Id not found!",  errorMessage);

    }



}