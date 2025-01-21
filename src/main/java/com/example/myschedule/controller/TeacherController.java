package com.example.myschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherController {

    @GetMapping("/teacher")
    public String showTeacherHomePage() {
        return "teacher-home-page";
    }

}
