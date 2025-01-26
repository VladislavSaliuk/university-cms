package com.example.myschedule.handler;


import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String username = authentication.getName();
        UserDTO userDTO = userService.getByUsername(username);

        HttpSession session = request.getSession();
        session.setAttribute("userId", userDTO.getUserId());

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String role = roles.stream()
                .filter(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_STUFF") || r.equals("ROLE_STUDENT") || r.equals("ROLE_TEACHER"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable role found"));

        switch (role) {
            case "ROLE_STUDENT" -> response.sendRedirect("/student");
            case "ROLE_TEACHER" -> response.sendRedirect("/teacher");
            case "ROLE_ADMIN" -> response.sendRedirect("/admin");
            case "ROLE_STUFF" -> response.sendRedirect("/stuff");
        }
    }
}
