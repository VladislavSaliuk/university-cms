package com.example.myschedule.controller;


import com.example.myschedule.converter.StatusConverter;
import com.example.myschedule.dto.*;
import com.example.myschedule.editor.*;
import com.example.myschedule.entity.DayOfWeek;
import com.example.myschedule.entity.Status;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final StatusConverter statusConverter;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Status.class, new StatusPropertyEditor(statusConverter));
    }
    @GetMapping("/admin")
    public String showAdminHomePage() {
        return "admin-home-page";
    }
    @GetMapping("/admin/dashboard")
    public String showAdminDashboardPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("userDTO", new UserDTO());
        return "admin-dashboard-page";
    }

    @PostMapping("/admin/dashboard/update")
    public String updateUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO , RedirectAttributes redirectAttributes) {
        try {

            userService.updateStatus(userDTO);

            String successMessage = "Status updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/admin/dashboard";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

}
