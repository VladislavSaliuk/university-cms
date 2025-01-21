package com.example.myschedule.service;

import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user details for username: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with username '{}' not found!", username);
            return new UsernameNotFoundException("User with " + username + " username not found!");
        });

        if (user.getStatus().name().equals(Status.BANNED.name())) {
            log.warn("User '{}' is banned!", username);
            throw new UserException("You are banned!");
        }

        log.info("Successfully loaded user details for username: {}", username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}