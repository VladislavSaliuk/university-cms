package com.example.myschedule.controller;

import com.example.myschedule.model.Group;
import com.example.myschedule.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    GroupService groupService;

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
