package com.example.universitycms.controller;

import com.example.universitycms.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GroupService groupService;

    @Test
    @WithMockUser(username = "stuff", roles = "STUFF")
    void showStuffGroupPage_shouldReturnStuffGroupPageView_whenUserIsStuff() throws Exception {

        when(groupService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-group-page"))
                .andExpect(model().attributeExists("groupList"));

    }


}
