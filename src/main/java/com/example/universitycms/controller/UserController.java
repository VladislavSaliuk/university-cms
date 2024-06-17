package com.example.universitycms.controller;


import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String showAdminUserPageView(Model model) {
        model.addAttribute(userService.getAll());
        return "admin-user-page";
    }

}
