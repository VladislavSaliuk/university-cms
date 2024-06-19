package com.example.universitycms.controller;


import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void showAdminUserPage_shouldReturnAdminUserPageView_whenUserIsAdmin() throws Exception {

        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-user-page"));

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
    void showStuffStudentPage_shouldReturnStuffStudentPageView_whenUserIsStuff() throws Exception {

        long roleId = 3;

        when(userService.getUsersByRole(roleId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stuff/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("stuff-student-page"))
                .andExpect(model().attributeExists("userList"));

    }




}
