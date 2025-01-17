package com.example.myschedule.service;

import com.example.myschedule.dto.StudentDTO;
import com.example.myschedule.dto.TeacherDTO;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;
    @Mock
    UserRepository userRepository;
    User user;
    TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {

        long userId = 1L;
        String username = "username";
        String password = "$2y$10$fGkGf4h1aCSlfPBpwTfvGerNI4puZTLVQeZLLCpSrqh8WcPuMtr7a";
        String email = "email@gmail.com";
        String firstname = "Firstname";
        String lastname = "Lastname";

        user = User.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.TEACHER)
                .status(Status.ACTIVE)
                .build();

        teacherDTO = TeacherDTO.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .status(Status.ACTIVE)
                .build();

    }

    @Test
    void getAllTeacher_shouldReturnTeacherDTOList() {

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<TeacherDTO> teacherDTOList = teacherService.getAllTeachers();

        assertNotNull(teacherDTOList);
        Assert.assertFalse(teacherDTOList.isEmpty());
        Assert.assertEquals(1, teacherDTOList.size());

        verify(userRepository).findAll();

    }

}
