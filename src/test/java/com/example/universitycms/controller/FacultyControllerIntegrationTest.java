package com.example.universitycms.controller;


import com.example.universitycms.model.Faculty;
import com.example.universitycms.service.FacultyService;
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

@WebMvcTest(FacultyController.class)
public class FacultyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    private static List<Faculty> facultyList = new LinkedList<>();
    @BeforeAll()
    static void init() {
        facultyList.add(new Faculty("Faculty name 1"));
        facultyList.add(new Faculty("Faculty name 2"));
        facultyList.add(new Faculty("Faculty name 3"));
        facultyList.add(new Faculty("Faculty name 4"));
        facultyList.add(new Faculty("Faculty name 5"));
    }

    @Test
    void showFacultyPage_showReturnFacultyPageViewWithCorrectFacultyList() throws Exception {

        given(facultyService.getAll()).willReturn(facultyList);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties_page"))
                .andExpect(model().attributeExists("facultyList"))
                .andExpect(model().attribute("facultyList",facultyList));

        verify(facultyService).getAll();

    }


}
