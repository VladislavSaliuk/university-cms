package com.example.universitycms.controller;


import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final long TEACHER_ROLE_ID = 2;
    private static final long STUDENT_ROLE_ID = 3;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationPage() {
        return "registration-page";
    }

    @GetMapping("/registered-student")
    public String showRegistrationPageForStudent(@ModelAttribute User user, Model model) {
        model.addAttribute("student", user);
        return "registered-student-page";
    }

    @PostMapping("/registered-student")
    public String registerStudent(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(STUDENT_ROLE_ID);
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/registered-teacher")
    public String showRegistrationPageForTeacher(@ModelAttribute User user, Model model) {
        model.addAttribute("teacher", user);
        return "registered-teacher-page";
    }

    @PostMapping("/registered-teacher")
    public String registerTeacher(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(TEACHER_ROLE_ID);
        userService.registerUser(user);
        return "redirect:/login";
    }


}
