package com.example.myschedule.controller;

import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_groups.sql", "/sql/insert_users.sql"})
public class AdminControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(roles = "ADMIN")
    public void showAdminHomePage_shouldReturnAdminHomePageView() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-home-page"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showAdminDashboardPage_shouldReturnAdminDashboardView() throws Exception {
        MvcResult result =mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userList"))
                .andExpect(view().name("admin-dashboard-page"))
                .andReturn();

        List<User> userList = (List<User>) result.getModelAndView().getModel().get("userList");

        Assert.assertNotNull(userList);
        Assert.assertFalse(userList.isEmpty());
        Assert.assertEquals(10, userList.size());

    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    @WithMockUser(roles = "ADMIN")
    public void updateUser_shouldReturnSuccessMessage(long userId) throws Exception {

        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .status(Status.BANNED)
                .build();

        MvcResult result = mockMvc.perform(post("/admin/dashboard/update")
                        .flashAttr("userDTO", userDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Status updated successfully!", successMessage);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateUser_shouldReturnErrorMessageWhenUserNotFound() throws Exception {

        UserDTO userDTO = UserDTO.builder()
                .userId(100L)
                .status(Status.BANNED)
                .build();

        MvcResult result = mockMvc.perform(post("/admin/dashboard/update")
                        .flashAttr("userDTO", userDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getFlashMap().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + userDTO.getUserId() + " Id not found!", errorMessage);

    }

}
