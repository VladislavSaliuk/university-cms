package com.example.myschedule.controller;

import com.example.myschedule.dto.RegistrationDTO;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration-page";
    }
    @GetMapping("/registration/register-student")
    public String showRegisterStudentPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register-student-page";
    }
    @GetMapping("/registration/register-teacher")
    public String showRegisterTeacherPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register-teacher-page";
    }
    @PostMapping("/registration/register-student")
    public String registerStudent(@Valid @ModelAttribute("registrationDTO") RegistrationDTO registrationDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        try {

            if (bindingResult.hasErrors()) {
                return "register-student-page";
            }

            registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

            userService.registerUser(registrationDTO);

            String successMessage = String.format("Welcome, %s!", registrationDTO.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/login";
        } catch (UserException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register-student-page";
        }
    }

    @PostMapping("/registration/register-teacher")
    public String registerTeacher(@Valid @ModelAttribute("registrationDTO") RegistrationDTO registrationDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        try {

            if (bindingResult.hasErrors()) {
                return "register-teacher-page";
            }

            registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

            userService.registerUser(registrationDTO);

            String successMessage = String.format("Welcome, %s!", registrationDTO.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register-teacher-page";
        }
    }

}
