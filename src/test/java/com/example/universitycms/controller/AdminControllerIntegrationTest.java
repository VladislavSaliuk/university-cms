package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.GroupService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    GroupService groupService;

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
    public void showAddCoursePage_shouldReturnAddCoursePageView_whenUserIsAdmin() throws Exception {

        mockMvc.perform(get("/admin/courses/add-course"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-course-page"))
                .andExpect(model().attributeExists("course"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminEditCoursePage_shouldReturnAdminCoursePageView() throws Exception {

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(courseService.getCourseByCourseId(1)).thenReturn(course);

        mockMvc.perform(get("/admin/courses/edit-course/{courseId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-edit-course-page"))
                .andExpect(model().attributeExists("course"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addCourse_shouldSaveCourse_whenUserIsAdmin() throws Exception {

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
    public void editCourse_shouldUpdateCourse_whenUserIsAdmin() throws Exception {

        Course course = new Course();
        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Course description");

        doNothing().when(courseService).updateCourse(course);

        mockMvc.perform(post("/admin/courses/edit-course/" + course.getCourseId()))
                .andExpect(redirectedUrl("/admin/courses"));

    }

    @Test
    @WithMockUser(username = "admin", roles="ADMIN")
    public void showGroupPage_shouldReturnAdminGroupPageView() throws Exception {

         mockMvc.perform(get("/admin/groups"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("admin-group-page"))
                 .andExpect(model().attributeExists("groupList"));


    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAddGroupPage_shouldReturnAddGroupPageView() throws Exception {

        mockMvc.perform(get("/admin/groups/add-group"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-group-page"))
                .andExpect(model().attributeExists("group"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addGroup_shouldSaveGroup_whenUserIsAdmin() throws Exception {

        doNothing().when(groupService).createGroup(any(Group.class));

        mockMvc.perform(post("/admin/groups/add-group")
                        .param("groupName", "Test group name"))
                .andExpect(redirectedUrl("/admin/groups"));

    }
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminEditGroupPage_shouldReturnAdminEditGroupPageView() throws Exception {

        Group group = new Group();

        group.setGroupId(1);
        group.setGroupName("Test group name");

        when(groupService.getGroupByGroupId(group.getGroupId())).thenReturn(group);

        mockMvc.perform(get("/admin/groups/edit-group/{groupId}", group.getGroupId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-edit-group-page"))
                .andExpect(model().attributeExists("group"));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteGroup_shouldRemoveGroup() throws Exception {

        Group group = new Group();
        group.setGroupId(1);
        group.setGroupName("Test group name");

        doNothing().when(groupService).removeGroupByGroupId(group.getGroupId());

        mockMvc.perform(get("/admin/groups/delete-group/" + group.getGroupId()))
                .andExpect(redirectedUrl("/admin/groups"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void editGroup_shouldUpdateGroup_whenUserIsAdmin() throws Exception {

        Group group = new Group();
        group.setGroupId(1);
        group.setGroupName("Test group name");

        doNothing().when(groupService).updateGroup(group);

        mockMvc.perform(post("/admin/groups/edit-group/" + group.getGroupId()))
                .andExpect(redirectedUrl("/admin/groups"));

    }



}
