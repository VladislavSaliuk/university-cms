package com.example.universitycms.controller;


import com.example.universitycms.service.GroupCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupCourseControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GroupCourseService groupCourseService;


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



}
