package com.example.universitycms.controller;

import com.example.universitycms.model.Subject;
import com.example.universitycms.service.SubjectService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
public class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    private static List<Subject> subjectList = new LinkedList<>();

    @BeforeAll()
    static void init() {
        subjectList.add(new Subject("Test subject name 1", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 2", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 3", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 4", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 5", "description", LocalTime.now()));
    }

    @Test
    void showSubjectPage_shouldReturnSubjectPageViewWithCorrectSubjectList() throws Exception {

        given(subjectService.getAll()).willReturn(subjectList);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects_page"))
                .andExpect(model().attributeExists("subjectList"))
                .andExpect(model().attribute("subjectList", subjectList));

        verify(subjectService).getAll();

    }


}
