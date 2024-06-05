package com.example.universitycms.controller;


import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import com.example.universitycms.service.RoleService;
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
@RequestMapping("/role-management")
public class RegistrationController {

    private static final long ADMIN_ROLE_ID = 1;
    private static final long TEACHER_ROLE_ID = 2;
    private static final long STUDENT_ROLE_ID = 3;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showSelectRolePage() {
        return "role-management-page";
    }

    @GetMapping("/student-registration")
    public String showRegistrationPageForStudent(@ModelAttribute User user, Model model) {
        model.addAttribute("student", user);
        return "student-registration-page";
    }

    @PostMapping("/student-registration")
    public String registerStudent(@ModelAttribute User user) {
        Role role = roleService.getRoleByRoleId(STUDENT_ROLE_ID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/teacher-registration")
    public String showRegistrationPageForTeacher(@ModelAttribute User user, Model model) {
        model.addAttribute("teacher", user);
        return "teacher-registration-page";
    }

    @PostMapping("/teacher-registration")
    public String registerTeacher(@ModelAttribute User user) {
        Role role = roleService.getRoleByRoleId(TEACHER_ROLE_ID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userService.registerUser(user);
        return "redirect:/login";
    }


}
