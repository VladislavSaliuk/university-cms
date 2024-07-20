package com.example.universitycms.controller;


import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentTeacherController {


    @Autowired
    private UserService userService;

    @GetMapping("/student/teachers")
    public String showStudentTeacherPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        List<User> userList = userService.getTeachersByUserId(userId);
        model.addAttribute("userList", userList);
        return "student-teacher-page";
    }




}
