package com.example.universitycms.controller;

import com.example.universitycms.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/subjects")
    public String showSubjectPage(Model model) {
        model.addAttribute("subjectList", subjectService.getAll());
        return "subjects_page";
    }

}
