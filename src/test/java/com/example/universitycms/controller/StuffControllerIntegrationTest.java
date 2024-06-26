package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.*;
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
public class StuffControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    GroupService groupService;

    @MockBean
    UserService userService;

    @MockBean
    UserCourseService userCourseService;

    @MockBean
    GroupCourseService groupCourseService;

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffGroupPage_shouldReturnStuffGroupPageView_whenUserIsStuff() throws Exception {

        when(groupService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-group-page"))
                .andExpect(model().attributeExists("groupList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffAssignCourseOnGroupPage_shouldReturnStuffAssignCourseOnGroupPageView() throws Exception {
        long groupId = 1;

        when(groupCourseService.getUnassignedCoursesForGroup(groupId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups/assign-course-on-group/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-assign-course-on-group-page"))
                .andExpect(model().attributeExists("courseList"));

    }


    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffRemoveCourseFromGroup_shouldReturnStuffRemoveCourseFromGroupPageView() throws Exception {

        long groupId = 1;

        when(groupCourseService.getAssignedCoursesForGroup(groupId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups/remove-course-from-group/{groupid}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-remove-course-from-group-page"))
                .andExpect(model().attributeExists("courseList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void assignCourseOnGroup_shouldInsertGroupCourseToDatabase() throws Exception {

        long groupId = 1;
        long courseId = 1;

        doNothing().when(groupCourseService).assignCourseOnGroup(groupId, courseId);

        mockMvc.perform(post("/stuff/groups/assign-course-on-group/" + groupId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/groups"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void removeCourseFromGroup_shouldDeleteGroupCourseFromDatabase() throws Exception {

        long groupId = 1;
        long courseId = 1;

        doNothing().when(groupCourseService).removeCourseFromGroup(groupId, courseId);

        mockMvc.perform(post("/stuff/groups/remove-course-from-group/" + groupId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/groups"));

    }

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

        when(userCourseService.getUnassignedCoursesForUser(userId)).thenReturn(Collections.emptyList());

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

        doNothing().when(userCourseService).removeUserFromCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/remove-teacher-from-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

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
    void assignTeacherOnCourse_shouldInsertUserCourseToDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).assignUserOnCourse(userId, courseId);

        mockMvc.perform(post("/stuff/teachers/assign-teacher-on-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/teachers"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffStudentPage_shouldReturnStuffStudentPageView_whenUserIsStuff() throws Exception {

        long roleId = 3;

        when(userService.getUsersByRole(roleId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-student-page"))
                .andExpect(model().attributeExists("userList"));

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
    void assignStudentOnCourse_shouldInsertUserCourseToDatabase() throws Exception {

        long userId = 1;
        long courseId = 1;

        doNothing().when(userCourseService).assignUserOnCourse(userId, courseId);

        mockMvc.perform(post("/stuff/students/assign-student-on-course/" + userId + "/" + courseId))
                .andExpect(redirectedUrl("/stuff/students"));

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
