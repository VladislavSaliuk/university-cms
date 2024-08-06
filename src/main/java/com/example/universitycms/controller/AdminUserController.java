package com.example.universitycms.controller;

import com.example.universitycms.model.User;
import com.example.universitycms.model.UserStatus;
import com.example.universitycms.model.Role;
import com.example.universitycms.service.UserService;
import com.example.universitycms.service.UserStatusService;
import com.example.universitycms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private RoleService roleService;

    @GetMapping("admin/users")
    public String showAdminUserPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("roleList", roleService.getAll());
        return "admin-user-page";
    }

    @PostMapping("admin/users/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateUserStatus(@RequestParam("userId") long userId, @RequestParam("status") String status) {
        try {
            User user = userService.getUserByUserId(userId);
            UserStatus userStatus = userStatusService.getUserStatusByUserStatusName(status);
            user.setUserStatus(userStatus);
            userService.changeUserStatus(user);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status");
        }
    }

    @PostMapping("admin/users/updateRole")
    @ResponseBody
    public ResponseEntity<?> updateUserRole(@RequestParam("userId") long userId, @RequestParam("role") String roleName) {
        try {
            User user = userService.getUserByUserId(userId);
            Role role = roleService.getRoleByRoleName(roleName);
            user.setRole(role);
            userService.changeUserRole(user);
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating role");
        }
    }

}
