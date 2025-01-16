package com.example.myschedule.service;


import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.dto.UpdateStatusDTO;
import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    User user;
    UserDTO userDTO;
    @BeforeEach
    void setUp() {

        String groupName = "name";

        Group group = Group.builder()
                .groupName(groupName)
                .build();

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
                .group(group)
                .build();

        userDTO = UserDTO.builder()
                .username(username)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void updateStatus_shouldReturnUserDTO() {

        UpdateStatusDTO updateStatusDTO = UpdateStatusDTO.builder()
                .userId(1L)
                .status(Status.BANNED)
                .build();

        when(userRepository.findById(updateStatusDTO.getUserId()))
                .thenReturn(Optional.of(user));

        UserDTO actualUserDTO = userService.updateStatus(updateStatusDTO);

        Assert.assertNotNull(actualUserDTO);
        Assert.assertEquals(updateStatusDTO.getStatus(), actualUserDTO.getStatus());

        verify(userRepository).findById(updateStatusDTO.getUserId());

    }

    @Test
    void updateStatus_shouldThrowException_whenUserNotFound() {

        UpdateStatusDTO updateStatusDTO = UpdateStatusDTO.builder()
                .userId(100L)
                .status(Status.BANNED)
                .build();

        when(userRepository.findById(updateStatusDTO.getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = Assert.assertThrows(UserNotFoundException.class, () -> userService.updateStatus(updateStatusDTO));

        Assert.assertEquals("User with " + updateStatusDTO.getUserId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(updateStatusDTO.getUserId());

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

        UserNotFoundException exception = Assert.assertThrows(UserNotFoundException.class, () -> userService.getById(userId));

        Assert.assertEquals("User with " + userId + " Id not found!", exception.getMessage());

        verify(userRepository).findById(userId);

    }

}
