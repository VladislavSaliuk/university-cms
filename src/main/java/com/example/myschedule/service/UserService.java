package com.example.myschedule.service;

import com.example.myschedule.dto.RegistrationDTO;
import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;

    public void registerUser(RegistrationDTO registrationDTO) {
        log.info("Starting user registration for username: {}", registrationDTO.getUsername());

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            log.warn("User with username '{}' already exists!", registrationDTO.getUsername());
            throw new UserException("User with " + registrationDTO.getUsername() + " username already exists!");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            log.warn("User with email '{}' already exists!", registrationDTO.getEmail());
            throw new UserException("User with " + registrationDTO.getEmail() + " E-mail already exists!");
        }

        if (registrationDTO.getRole() == Role.STUFF || registrationDTO.getRole() == Role.ADMIN) {
            log.warn("Attempt to assign forbidden role '{}' for username: {}", registrationDTO.getRole(), registrationDTO.getUsername());
            throw new UserException("This role is not allowed for you!");
        }

        log.debug("Creating user object for username: {}", registrationDTO.getUsername());
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .password(registrationDTO.getPassword())
                .email(registrationDTO.getEmail())
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .role(registrationDTO.getRole())
                .status(registrationDTO.getStatus())
                .build();

        log.info("Saving user to the database for username: {}", registrationDTO.getUsername());
        userRepository.save(user);
        log.info("User registration successful for username: {}", registrationDTO.getUsername());
    }

    @Transactional
    public UserDTO updateStatus(UserDTO userDTO) {
        log.info("Attempting to update status for user with ID: {}", userDTO.getUserId());

        User updatedUser = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found!", userDTO.getUserId());
                    return new UserNotFoundException("User with " + userDTO.getUserId() + " Id not found!");
                });

        updatedUser.setStatus(userDTO.getStatus());
        log.info("Successfully updated status for user with ID: {} to {}", updatedUser.getUserId(), updatedUser.getStatus());

        return UserDTO.toUserDTO(updatedUser);
    }

    public List<UserDTO> getAll() {
        log.info("Fetching all users");

        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(UserDTO::toUserDTO)
                .collect(Collectors.toList());

        Collections.reverse(users);

        log.info("Fetched {} users", users.size());
        return users;
    }

    public UserDTO getById(long userId) {
        log.info("Fetching user with ID: {}", userId);

        return userRepository.findById(userId)
                .map(user -> {
                    log.info("User found with ID: {}", userId);
                    return UserDTO.toUserDTO(user);
                })
                .orElseThrow(() -> {
                    log.error("User with ID {} not found!", userId);
                    return new UserNotFoundException("User with " + userId + " Id not found!");
                });
    }
    public UserDTO getByUsername(String username) {
        log.info("Searching for user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.info("User found: {}", user.getUsername());
                    return UserDTO.toUserDTO(user);
                })
                .orElseThrow(() -> {
                    log.error("User with username '{}' not found!", username);
                    return new UsernameNotFoundException("User with " + username + " username not found!");
                });
    }

}