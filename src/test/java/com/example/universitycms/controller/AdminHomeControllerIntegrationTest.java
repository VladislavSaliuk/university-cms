package com.example.universitycms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminHomeControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminHomePage_shouldReturnAdminHomePageView() throws Exception {

        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-home-page"));

    }

}
