package com.example.universitycms.controller;

import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/admin/users")
    public String showAdminUserPage(Model model) {
        model.addAttribute(userService.getAll());
        return "admin-user-page";
    }

    @GetMapping("/admin/courses")
    public String showAdminCoursePage(Model model) {
        model.addAttribute(courseService.getAll());
        return "admin-course-page";
    }


}
