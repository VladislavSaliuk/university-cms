package com.example.universitycms.controller;

import javax.validation.Valid;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/admin/courses/add-course")
    public String showAddCoursePage(@ModelAttribute Course course, Model model) {
        model.addAttribute("course", course);
        return "add-course-page";
    }

    @PostMapping("/admin/courses/add-course")
    public String createCourse( @ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/admin/courses";
    }

}
