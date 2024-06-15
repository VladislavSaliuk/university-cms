package com.example.universitycms.controller;

import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.RoleService;
import com.example.universitycms.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminUserPage_shouldReturnAdminUserPage() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-user-page"))
                .andExpect(model().attributeExists("userList"));

    }
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void showAdminCoursePage_shouldReturnAdminCoursePageView() throws Exception {

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-course-page"))
                .andExpect(model().attributeExists("courseList"));

    }


}
