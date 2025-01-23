package com.example.myschedule.service;


import com.example.myschedule.dto.RegistrationDTO;
import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    User user;
    UserDTO userDTO;
    RegistrationDTO registrationDTO;
    @BeforeEach
    void setUp() {

        String username = "username";
        String password = "$2y$10$fGkGf4h1aCSlfPBpwTfvGerNI4puZTLVQeZLLCpSrqh8WcPuMtr7a";
        String email = "email@gmail.com";
        String firstname = "Firstname";
        String lastname = "Lastname";

        user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();

        userDTO = UserDTO.builder()
                .username(username)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();

        registrationDTO = RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();

    }

    @Test
    void registerUser_shouldSaveUser() {

        when(userRepository.existsByUsername(registrationDTO.getUsername()))
                .thenReturn(false);

        when(userRepository.existsByEmail(registrationDTO.getEmail()))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        userService.registerUser(registrationDTO);

        verify(userRepository).existsByUsername(registrationDTO.getUsername());
        verify(userRepository).existsByEmail(registrationDTO.getEmail());
        verify(userRepository).save(any(User.class));

    }
    @Test
    void registerUser_shouldThrowException_whenUsernameRepeats() {

        when(userRepository.existsByUsername(registrationDTO.getUsername()))
                .thenReturn(true);

        UserException exception = assertThrows(UserException.class, () -> userService.registerUser(registrationDTO));

        assertEquals("User with " + registrationDTO.getUsername() + " username already exists!", exception.getMessage());

        verify(userRepository).existsByUsername(registrationDTO.getUsername());
        verify(userRepository,never()).existsByEmail(registrationDTO.getEmail());
        verify(userRepository,never()).save(any(User.class));

    }

    @Test
    void registerUser_shouldThrowException_whenEmailRepeats() {

        when(userRepository.existsByUsername(registrationDTO.getUsername()))
                .thenReturn(false);

        when(userRepository.existsByEmail(registrationDTO.getEmail()))
                .thenReturn(true);

        UserException exception = assertThrows(UserException.class, () -> userService.registerUser(registrationDTO));

        assertEquals("User with " + registrationDTO.getEmail() + " E-mail already exists!", exception.getMessage());

        verify(userRepository).existsByUsername(registrationDTO.getUsername());
        verify(userRepository).existsByEmail(registrationDTO.getEmail());
        verify(userRepository,never()).save(any(User.class));

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN", "STUFF"})
    void registerUser_shouldThrowException_whenUserContainsNotAvailableStatus(Role role) {

        registrationDTO.setRole(role);

        when(userRepository.existsByUsername(registrationDTO.getUsername()))
                .thenReturn(false);

        when(userRepository.existsByEmail(registrationDTO.getEmail()))
                .thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> userService.registerUser(registrationDTO));

        assertEquals("This role is not allowed for you!", exception.getMessage());

        verify(userRepository).existsByUsername(registrationDTO.getUsername());
        verify(userRepository).existsByEmail(registrationDTO.getEmail());
        verify(userRepository,never()).save(any(User.class));

    }

    @Test
    void updateStatus_shouldReturnUserDTO() {

        userDTO.setStatus(Status.BANNED);

        when(userRepository.findById(userDTO.getUserId()))
                .thenReturn(Optional.of(user));

        UserDTO actualUserDTO = userService.updateStatus(userDTO);

        Assert.assertNotNull(actualUserDTO);
        Assert.assertEquals(userDTO.getStatus(), actualUserDTO.getStatus());

        verify(userRepository).findById(userDTO.getUserId());

    }

    @Test
    void updateStatus_shouldThrowException_whenUserNotFound() {

        userDTO.setStatus(Status.BANNED);

        when(userRepository.findById(userDTO.getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateStatus(userDTO));

        Assert.assertEquals("User with " + userDTO.getUserId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(userDTO.getUserId());

    }

    @Test
    void getAll_shouldReturnUserDTOList() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDTO> userDTOList = userService.getAll();

        Assert.assertNotNull(userDTOList);
        Assert.assertFalse(userDTOList.isEmpty());
        Assert.assertEquals(1, userDTOList.size());

        verify(userRepository).findAll();

    }

    @Test
    void getById_shouldReturnUserDTO() {

        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserDTO actualUserDTO = userService.getById(userId);

        Assert.assertNotNull(actualUserDTO);
        Assert.assertEquals(userDTO, actualUserDTO);

        verify(userRepository).findById(userId);

    }

    @Test
    void getById_shouldThrowException_whenUserNotFound() {

        long userId = 100L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getById(userId));

        Assert.assertEquals("User with " + userId + " Id not found!", exception.getMessage());

        verify(userRepository).findById(userId);

    }

    @Test
    void getByUsername_shouldReturnUserDTO() {

        String username = "Username";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserDTO actualUserDTO = userService.getByUsername(username);

        Assert.assertNotNull(actualUserDTO);
        Assert.assertEquals(userDTO, actualUserDTO);

        verify(userRepository).findByUsername(username);

    }

    @Test
    void getByUsername_shouldThrowException_whenUserNotFound() {

        String username = "Username";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(username));

        Assert.assertEquals("User with " + username + " username not found!", exception.getMessage());

        verify(userRepository).findByUsername(username);

    }


}
