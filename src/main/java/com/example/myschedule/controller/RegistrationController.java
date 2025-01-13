package com.example.myschedule.controller;

import com.example.myschedule.dto.UserRegistrationDTO;
import com.example.myschedule.model.RoleId;
import com.example.myschedule.model.User;
import com.example.myschedule.model.UserStatusId;
import com.example.myschedule.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration-page";
    }

    @GetMapping("/registration/student")
    public String showRegistrationPageForStudent(Model model) {
        model.addAttribute("userDTO", new UserRegistrationDTO());
        return "register-student-page";
    }

    @PostMapping("/registration/student")
    public String registerStudent(@Valid @ModelAttribute("userDTO") UserRegistrationDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        return registerUser(userDTO, bindingResult, model, redirectAttributes, RoleId.STUDENT);
    }

    @GetMapping("/registration/teacher")
    public String showRegistrationPageForTeacher(Model model) {
        model.addAttribute("userDTO", new UserRegistrationDTO());
        return "register-teacher-page";
    }

    @PostMapping("/registration/teacher")
    public String registerTeacher(@Valid @ModelAttribute("userDTO") UserRegistrationDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        return registerUser(userDTO, bindingResult, model, redirectAttributes, RoleId.TEACHER);
    }

    private String registerUser(UserRegistrationDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, RoleId roleId) {
        if (bindingResult.hasErrors()) {
            return getPageForRole(roleId);
        }

        if (!isPasswordMatching(userDTO)) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            return getPageForRole(roleId);
        }

        User user = createUserFromDTO(userDTO, roleId);
        return processUserRegistration(user, model, redirectAttributes, roleId);
    }

    private boolean isPasswordMatching(UserRegistrationDTO userDTO) {
        return userDTO.getPassword().equals(userDTO.getConfirmPassword());
    }

    private User createUserFromDTO(UserRegistrationDTO userDTO, RoleId roleId) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleId.getValue());
        user.setUserStatus(UserStatusId.ACTIVE.getValue());
        return user;
    }

    private String processUserRegistration(User user, Model model, RedirectAttributes redirectAttributes, RoleId roleId) {
        try {
            userService.registerUser(user);
            String successMessage = String.format("Welcome, %s!", user.getUserName());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
            return getPageForRole(roleId);
        }
    }

    private String getPageForRole(RoleId roleId) {
        return roleId == RoleId.STUDENT ? "register-student-page" : "register-teacher-page";
    }

}