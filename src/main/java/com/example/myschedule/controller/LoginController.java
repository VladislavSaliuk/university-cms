package com.example.myschedule.controller;

import com.example.myschedule.exception.UserException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login-page";
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login-page";
    }
    @ExceptionHandler(UserException.class)
    public String handleUserException(UserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login-page";
    }

}
