package com.example.universitycms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    @GetMapping("/admin/home")
    public String showAdminHomePage() {
        return "admin-home-page";
    }


}
