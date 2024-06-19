package com.example.universitycms.controller;


import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.PrivilegedAction;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    private final  long ADMIN_ROLE_ID = 1;
    private final long TEACHER_ROLE_ID = 2;
    private final long STUDENT_ROLE_ID = 3;
    private final long STUFF_ROLE_ID = 4;

    @GetMapping("/admin/users")
    public String showAdminUserPage(Model model) {
        model.addAttribute(userService.getAll());
        return "admin-user-page";
    }
    @GetMapping("/stuff/teachers")
    public String showStuffTeacherPage(Model model) {
        List<User> userList = userService.getUsersByRole(TEACHER_ROLE_ID);
        model.addAttribute("userList", userList);
        return "stuff-teacher-page";
    }
    @GetMapping("/stuff/students")
    public String showStuffStudentPage(Model model) {
        List<User> userList = userService.getUsersByRole(STUDENT_ROLE_ID);
        model.addAttribute("userList", userList);
        return "stuff-student-page";
    }


}
