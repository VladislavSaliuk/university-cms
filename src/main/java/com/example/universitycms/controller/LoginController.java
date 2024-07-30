package com.example.universitycms.controller;

import com.example.universitycms.exception.UserNotFoundException;
import com.example.universitycms.exception.UserStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String showLoginPage() {
        return "login-page";
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        logger.error(e.getMessage());
        return "login-page";
    }
    @ExceptionHandler(UserStatusException.class)
    public String handleUserStatusException(UserStatusException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        logger.error(e.getMessage());
        return "login-page";
    }


}
