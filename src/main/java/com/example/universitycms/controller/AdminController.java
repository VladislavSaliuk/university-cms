package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/admin/courses/course-form")
    public String showCourseFormPage(@ModelAttribute Course course, Model model) {
        model.addAttribute("course", course);
        return "course-form-page";
    }

    @PostMapping("/admin/courses/add-course")
    public String createCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/courses/delete-course/{courseId}")
    public String deleteCourse(@PathVariable long courseId) {
        courseService.removeCourseByCourseId(courseId);
        return "redirect:/admin/courses";
    }

}
