package com.example.myschedule.controller;


import com.example.myschedule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    @GetMapping("/admin")
    public String showAdminHomePage() {
        return "admin-home-page";
    }
    @GetMapping("/admin/dashboard")
    public String showAdminDashboardPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        return "admin-dashboard-page";
    }

}
