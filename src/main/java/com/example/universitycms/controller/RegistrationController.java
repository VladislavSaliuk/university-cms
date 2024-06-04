package com.example.universitycms.controller;


import com.example.universitycms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/select-role")
public class RegistrationController {


    @GetMapping
    public String showSelectRolePage() {
        return "select-role-page";
    }

    @GetMapping("/register-student")
    public String showRegistrationPageForStudent() {
        return "registration-page";
    }

    @GetMapping("/register-teacher")
    public String showRegistrationPageForTeacher() {
        return "registration-page";
    }


}
