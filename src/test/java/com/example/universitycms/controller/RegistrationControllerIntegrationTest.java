package com.example.universitycms.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    public void showSelectRolePage_shouldReturnSelectRolePageView() throws Exception {
        mockMvc.perform(get("/role-management"))
                .andExpect(status().isOk())
                .andExpect(view().name("select-role-page"))
                .andReturn();
    }

    @Test
    public void testShowRegistrationPageForStudent() throws Exception {
        mockMvc.perform(get("/role-management/student-registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-page"));
    }

    @Test
    public void testShowRegistrationPageForTeacher() throws Exception {
        mockMvc.perform(get("/role-management/teacher-registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-page"));
    }


}
