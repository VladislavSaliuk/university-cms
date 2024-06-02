package com.example.universitycms.controller;

import com.example.universitycms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String showStudentsPage(Model model) {
        model.addAttribute("studentList", studentService.getAll());
        return "students-page";
    }


}
