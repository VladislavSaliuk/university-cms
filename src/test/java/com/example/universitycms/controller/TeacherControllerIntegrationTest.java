package com.example.universitycms.controller;


import com.example.universitycms.model.Faculty;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Teacher;
import com.example.universitycms.service.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    private static List<Teacher> teacherList = new LinkedList<>();
    @BeforeAll()
    static void init() {

        Faculty faculty = new Faculty("Test faculty");
        Group group = new Group("Test group");

        teacherList.add(new Teacher("Test login 1" , "Test password 1" , "Test email 1"));
        teacherList.add(new Teacher("Test login 2" , "Test password 2" , "Test email 2"));
        teacherList.add(new Teacher("Test login 3" , "Test password 3" , "Test email 3"));
        teacherList.add(new Teacher("Test login 4" , "Test password 4" , "Test email 4"));
        teacherList.add(new Teacher("Test login 5" , "Test password 5" , "Test email 5"));


    }

    @Test
    void showTeachersPage_shouldReturnTeachersPageViewWithCorrectTeacherList() throws Exception {

        given(teacherService.getAll()).willReturn(teacherList);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers_page"))
                .andExpect(model().attributeExists("teacherList"))
                .andExpect(model().attribute("teacherList", teacherList));

        verify(teacherService).getAll();

    }

}
