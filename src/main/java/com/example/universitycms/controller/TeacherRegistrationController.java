package com.example.universitycms.controller;


import com.example.universitycms.model.Teacher;
import com.example.universitycms.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TeacherRegistrationController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/register")
    public String showTeacherRegistrationPage(@ModelAttribute Teacher teacher, Model model) {
        model.addAttribute("teacher", teacher);
        return "teacher-registration-page";
    }
    @PostMapping("/register")
    public void registerTeacher(@ModelAttribute Teacher teacher) {
        System.out.println(teacher);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherService.addTeacher(teacher);
    }

}
