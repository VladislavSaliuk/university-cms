package com.example.universitycms.controller;


import com.example.universitycms.model.Faculty;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Student;
import com.example.universitycms.service.StudentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private static List<Student> studentList = new LinkedList<>();

    @BeforeAll()
    static void init() {

        Faculty faculty = new Faculty("Test faculty");
        Group group = new Group("Test group");

        studentList.add(new Student("Test login 1" , "Test password 1" , "Test email 1"));
        studentList.add(new Student("Test login 2" , "Test password 2" , "Test email 2"));
        studentList.add(new Student("Test login 3" , "Test password 3" , "Test email 3"));
        studentList.add(new Student("Test login 4" , "Test password 4" , "Test email 4"));
        studentList.add(new Student("Test login 5" , "Test password 5" , "Test email 5"));


    }

    @Test
    void showStudentsPage_shouldReturnStudentPageViewWithCorrectStudentList() throws Exception {

        given(studentService.getAll()).willReturn(studentList);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students_page"))
                .andExpect(model().attributeExists("studentList"))
                .andExpect(model().attribute("studentList", studentList));

        verify(studentService).getAll();

    }

}
