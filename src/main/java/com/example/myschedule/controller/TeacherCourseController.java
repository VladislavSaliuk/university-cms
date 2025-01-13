package com.example.myschedule.controller;


import com.example.myschedule.model.Course;
import com.example.myschedule.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TeacherCourseController {

    @Autowired
    private UserService userService;

    @GetMapping("/teacher/courses")
    public String showTeacherCoursePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        List<Course> courseList = userService.getAllCoursesForTeacherByUserId(userId);
        model.addAttribute("courseList", courseList);
        return "teacher-course-page";
    }


}
