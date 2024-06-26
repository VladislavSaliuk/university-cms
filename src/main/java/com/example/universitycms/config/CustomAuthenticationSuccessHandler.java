package com.example.universitycms.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String role = roles.stream()
                .filter(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_STUFF") || r.equals("ROLE_STUDENT") || r.equals("ROLE_TEACHER"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable role found"));

        switch (role) {
            case "ROLE_ADMIN" -> response.sendRedirect("/admin/courses");
            case "ROLE_STUFF" -> response.sendRedirect("/stuff/teachers");
            default -> response.sendRedirect("/home");
        }
    }
}
