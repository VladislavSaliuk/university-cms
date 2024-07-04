package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import com.example.universitycms.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentCourseController {

    @Autowired
    private UserService userService;

    @GetMapping("/student/courses")
    public String showStudentCoursePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        List<Course> courseList = userService.getAllCoursesByUserId(userId);
        model.addAttribute("courseList", courseList);
        return "student-course-page";
    }



}
