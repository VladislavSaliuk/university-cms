package com.example.universitycms.controller;


import com.example.universitycms.model.User;
import com.example.universitycms.model.UserStatusId;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private static final long TEACHER_ROLE_ID = 2;
    private static final long STUDENT_ROLE_ID = 3;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration-page";
    }

    @GetMapping("/registration/student")
    public String showRegistrationPageForStudent(@ModelAttribute User user, Model model) {
        model.addAttribute("student", user);
        return "register-student-page";
    }

    @PostMapping("/registration/student")
    public String registerStudent(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(STUDENT_ROLE_ID);
        user.setUserStatus(UserStatusId.ACTIVE.getValue());
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/registration/teacher")
    public String showRegistrationPageForTeacher(@ModelAttribute User user, Model model) {
        model.addAttribute("teacher", user);
        return "register-teacher-page";
    }

    @PostMapping("/registration/teacher")
    public String registerTeacher(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(TEACHER_ROLE_ID);
        userService.registerUser(user);
        return "redirect:/login";
    }


}
