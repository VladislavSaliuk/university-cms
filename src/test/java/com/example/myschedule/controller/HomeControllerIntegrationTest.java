package com.example.myschedule.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showIndexPage_shouldReturnIndexPageView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void showAboutMePage_shouldReturnAboutMePageView() throws Exception {
        mockMvc.perform(get("/about-me"))
                .andExpect(status().isOk())
                .andExpect(view().name("about-me"));
    }

    @Test
    public void showFeaturesPage_shouldReturnFeaturesPageView() throws Exception {
        mockMvc.perform(get("/features"))
                .andExpect(status().isOk())
                .andExpect(view().name("features"));
    }

    @Test
    public void showContactMePage_shouldReturnContactMePageView() throws Exception {
        mockMvc.perform(get("/contact-me"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact-me"));
    }


}
