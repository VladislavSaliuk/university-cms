package com.example.universitycms.controller;

import com.example.universitycms.model.Group;
import com.example.universitycms.service.GroupService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GroupController.class)
public class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;
    private static List<Group> groupList = new LinkedList<>();
    @BeforeAll()
    static void init() {
        groupList.add(new Group("Test group 1"));
        groupList.add(new Group("Test group 2"));
        groupList.add(new Group("Test group 3"));
        groupList.add(new Group("Test group 4"));
        groupList.add(new Group("Test group 5"));
    }

    @Test
    void showGroupPage_shouldReturnGroupPageViewWithCorrectGroupList() throws Exception {

        given(groupService.getAll()).willReturn(groupList);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups_page"))
                .andExpect(model().attributeExists("groupList"))
                .andExpect(model().attribute("groupList", groupList));

        verify(groupService).getAll();

    }

}
