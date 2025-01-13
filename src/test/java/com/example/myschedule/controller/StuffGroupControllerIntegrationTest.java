package com.example.myschedule.controller;


import com.example.myschedule.model.Group;
import com.example.myschedule.service.GroupCourseService;
import com.example.myschedule.service.GroupService;
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
public class StuffGroupControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    GroupService groupService;

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
    public void showAddGroupPage_shouldReturnAddGroupPageView() throws Exception {

        mockMvc.perform(get("/stuff/groups/add-group"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-group-page"))
                .andExpect(model().attributeExists("group"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void showStuffEditGroupPage_shouldReturnStuffEditGroupPageView() throws Exception {

        Group group = new Group();

        group.setGroupId(1);
        group.setGroupName("Test group name");

        when(groupService.getGroupByGroupId(group.getGroupId())).thenReturn(group);

        mockMvc.perform(get("/stuff/groups/edit-group/{groupId}", group.getGroupId()))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-edit-group-page"))
                .andExpect(model().attributeExists("group"));

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
    void showStuffAssignUserOnGroupPage_shouldReturnStuffAssignUserOnGroupPageView() throws Exception {
        long groupId = 1;

        when(groupService.getUnassignedUsersToGroup(groupId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups/assign-user-on-group/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-assign-user-on-group-page"))
                .andExpect(model().attributeExists("userList"));

    }


    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffRemoveUserFromGroupPage_shouldReturnStuffRemoveUserFromGroupPageView() throws Exception {

        long groupId = 1;

        when(groupService.getAssignedUsersToGroup(groupId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups/remove-user-from-group/{groupId}", groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-remove-user-from-group-page"))
                .andExpect(model().attributeExists("userList"));

    }

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void addGroup_shouldSaveGroup_whenUserIsStuff() throws Exception {

        doNothing().when(groupService).createGroup(any(Group.class));

        mockMvc.perform(post("/stuff/groups/add-group")
                        .param("groupName", "Test group name"))
                .andExpect(redirectedUrl("/stuff/groups"));

    }
    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    public void editGroup_shouldUpdateGroup_whenUserIsStuff() throws Exception {

        Group group = new Group();
        group.setGroupId(1);
        group.setGroupName("Test group name");

        doNothing().when(groupService).updateGroup(group);

        mockMvc.perform(post("/stuff/groups/edit-group/" + group.getGroupId()))
                .andExpect(redirectedUrl("/stuff/groups"));

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
    void assignUserOnGroup_shouldInsertGroupIdToUserTable() throws Exception {

        long groupId = 1;
        long userId = 1;

        doNothing().when(groupService).assignUserToGroup(groupId, userId);

        mockMvc.perform(post("/stuff/groups/assign-user-on-group/" + groupId + "/" + userId))
                .andExpect(redirectedUrl("/stuff/groups"));

    }


    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void removeUserFromGroup_shouldDeleteGroupIdFromUserTable() throws Exception {

        long groupId = 1;
        long userId = 1;

        doNothing().when(groupService).removeUserFromGroup(groupId, userId);

        mockMvc.perform(post("/stuff/groups/remove-user-from-group/" + groupId + "/" + userId))
                .andExpect(redirectedUrl("/stuff/groups"));

    }

}
