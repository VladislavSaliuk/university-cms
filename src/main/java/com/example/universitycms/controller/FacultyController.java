package com.example.universitycms.controller;

import com.example.universitycms.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/faculties")
    public String showFacultyPage(Model model) {
        model.addAttribute("facultyList", facultyService.getAll());
        return "faculties_page";
    }

}
