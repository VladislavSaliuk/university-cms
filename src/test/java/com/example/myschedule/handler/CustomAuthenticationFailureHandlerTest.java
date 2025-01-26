package com.example.myschedule.handler;

import com.example.myschedule.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationFailureHandlerTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession httpSession;

    @ParameterizedTest
    @ValueSource(strings = {"Bad credentials", "You are banned!", "User with test username not found!"})
    void onAuthenticationFailure_shouldHandleDifferentExceptions(String errorMessage) throws IOException {

        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
        BadCredentialsException exception = new  BadCredentialsException(errorMessage);

        when(request.getSession()).thenReturn(httpSession);

        failureHandler.onAuthenticationFailure(request, response, exception);

        verify(request).getSession();
        verify(request.getSession()).setAttribute("errorMessage", errorMessage);
        verify(response).sendRedirect("/login");
    }

}
