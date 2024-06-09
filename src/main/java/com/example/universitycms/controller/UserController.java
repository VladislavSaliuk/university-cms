package com.example.universitycms.controller;

import com.example.universitycms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin-page")
    public String showAdminPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        return "admin-page";
    }


}
