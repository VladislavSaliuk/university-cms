package com.example.universitycms.config;

import com.example.universitycms.model.User;
import com.example.universitycms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getUserId());

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String role = roles.stream()
                .filter(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_STUFF") || r.equals("ROLE_STUDENT") || r.equals("ROLE_TEACHER"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable role found"));

        switch (role) {
            case "ROLE_STUDENT" -> response.sendRedirect("/student/courses");
            case "ROLE_TEACHER" -> response.sendRedirect("/teacher/courses");
            case "ROLE_ADMIN" -> response.sendRedirect("/admin/courses");
            case "ROLE_STUFF" -> response.sendRedirect("/stuff/teachers");
            default -> response.sendRedirect("/home");
        }
    }
}
