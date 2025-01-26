package com.example.myschedule.handler;

import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Role;
import com.example.myschedule.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

    @Mock
    UserService userService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession session;

    @Mock
    Authentication authentication;

    @InjectMocks
    CustomAuthenticationSuccessHandler successHandler;

    @Test
    void onAuthenticationSuccess_shouldRedirectToAdminRole() throws IOException {

        String username = "username";
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.ADMIN);
        userDTO.setUserId(4L);

        when(authentication.getName()).thenReturn(username);
        when(userService.getByUsername(username)).thenReturn(userDTO);
        when(request.getSession()).thenReturn(session);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(simpleGrantedAuthority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(session).setAttribute("userId", 4L);
        verify(response).sendRedirect("/admin");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectToStuffRole() throws IOException {

        String username = "username";
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.STUFF);
        userDTO.setUserId(4L);

        when(authentication.getName()).thenReturn(username);
        when(userService.getByUsername(username)).thenReturn(userDTO);
        when(request.getSession()).thenReturn(session);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_STUFF");
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(simpleGrantedAuthority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(session).setAttribute("userId", 4L);
        verify(response).sendRedirect("/stuff");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectToTeacherRole() throws IOException {

        String username = "username";
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.TEACHER);
        userDTO.setUserId(4L);

        when(authentication.getName()).thenReturn(username);
        when(userService.getByUsername(username)).thenReturn(userDTO);
        when(request.getSession()).thenReturn(session);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_TEACHER");
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(simpleGrantedAuthority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(session).setAttribute("userId", 4L);
        verify(response).sendRedirect("/teacher");
    }

    @Test
    void onAuthenticationSuccess_shouldRedirectToStudentRole() throws IOException {

        String username = "username";
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.STUDENT);
        userDTO.setUserId(4L);

        when(authentication.getName()).thenReturn(username);
        when(userService.getByUsername(username)).thenReturn(userDTO);
        when(request.getSession()).thenReturn(session);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_STUDENT");
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(simpleGrantedAuthority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(session).setAttribute("userId", 4L);
        verify(response).sendRedirect("/student");
    }

    @Test
    void onAuthenticationSuccess_shouldThrowExceptionIfNoRoleFound() {

        String username = "username";
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(3L);

        when(authentication.getName()).thenReturn(username);
        when(userService.getByUsername(username)).thenReturn(userDTO);
        when(request.getSession()).thenReturn(session);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE");
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(simpleGrantedAuthority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                successHandler.onAuthenticationSuccess(request, response, authentication)
        );
        assertEquals("No suitable role found", exception.getMessage());
    }

}
