package com.example.universitycms.controller;

import com.example.universitycms.service.RoleService;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-page")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String showAdminPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("roleList", roleService.getAll());
        return "admin-page";
    }

}
