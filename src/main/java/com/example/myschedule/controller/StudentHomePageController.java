package com.example.myschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentHomePageController {

    @GetMapping("/student/home")
    public String showStudentHomePage() {
        return "student-home-page";
    }


}
