package com.example.universitycms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/admin-page")
    public String showAdminPage() {
        return "admin-page";
    }


}
