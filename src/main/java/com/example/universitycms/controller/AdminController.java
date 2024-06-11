package com.example.universitycms.controller;

import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import com.example.universitycms.service.RoleService;
import com.example.universitycms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("roleList", roleService.getAll());
        return "admin-page";
    }

    @PostMapping("/admin/update-role")
    public String updateRole(@RequestParam long userId, @RequestParam String roleId) {
        long roleIdLong = Long.parseLong(roleId);
        User user = userService.getUserByUserId(userId);
        Role role = roleService.getRoleByRoleId(roleIdLong);
        user.setRole(role.getRoleId());
        userService.updateUser(user);
        return "redirect:/admin";
    }
}
