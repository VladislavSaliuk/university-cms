package com.example.universitycms.controller;


import com.example.universitycms.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    public String showTeacherPage(Model model) {
        model.addAttribute("teacherList", teacherService.getAll());
        return "teachers-page";
    }

}
