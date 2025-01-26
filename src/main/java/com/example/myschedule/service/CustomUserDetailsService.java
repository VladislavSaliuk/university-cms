package com.example.myschedule.service;

import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user details for username: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with username '{}' not found!", username);
            return new BadCredentialsException("User with " + username + " username not found!");
        });

        if (user.getStatus().name().equals(Status.BANNED.name())) {
            log.warn("User '{}' is banned!", username);
            throw new BadCredentialsException("You are banned!");
        }

        log.info("Successfully loaded user details for username: {}", username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}
