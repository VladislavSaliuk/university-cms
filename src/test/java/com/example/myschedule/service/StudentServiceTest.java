package com.example.myschedule.service;

import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.dto.StudentDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.GroupRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @InjectMocks
    StudentService studentService;
    @Mock
    UserRepository userRepository;
    @Mock
    GroupRepository groupRepository;
    User user;
    StudentDTO studentDTO;
    Group group;
    GroupDTO groupDTO;
    @BeforeEach
    void setUp() {

        long groupId = 1;
        String groupName = "name";

        group = Group.builder()
                .groupId(groupId)
                .groupName(groupName)
                .build();

        groupDTO = GroupDTO.builder()
                .groupId(groupId)
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
                .build();

        studentDTO = StudentDTO.builder()
                .username(username)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .status(Status.ACTIVE)
                .groupDTO(groupDTO)
                .build();
    }
    @Test
    void getAllStudents_shouldReturnStudentDTOList() {

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<StudentDTO> studentDTOList = studentService.getAllStudents();

        assertNotNull(studentDTOList);
        Assert.assertFalse(studentDTOList.isEmpty());
        Assert.assertEquals(1, studentDTOList.size());

        verify(userRepository).findAll();

    }

    @Test
    void assignStudentToGroup_shouldReturnStudentDTO() {

        when(userRepository.findById(studentDTO.getUserId()))
                .thenReturn(Optional.of(user));

        when(groupRepository.findById(studentDTO.getGroupDTO().getGroupId()))
                .thenReturn(Optional.of(group));

        StudentDTO actualStudentDTO = studentService.assignStudentToGroup(studentDTO);

        assertNotNull(actualStudentDTO);
        assertEquals(studentDTO, actualStudentDTO);

        verify(userRepository).findById(studentDTO.getUserId());
        verify(groupRepository).findById(studentDTO.getGroupDTO().getGroupId());

    }

    @Test
    void assignStudentToGroup_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(studentDTO.getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> studentService.assignStudentToGroup(studentDTO));

        assertEquals("Student with " + studentDTO.getUserId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(studentDTO.getUserId());
        verify(groupRepository, never()).findById(studentDTO.getGroupDTO().getGroupId());

    }

    @Test
    void assignStudentToGroup_shouldThrowException_whenGroupNotFound() {

        when(userRepository.findById(studentDTO.getUserId()))
                .thenReturn(Optional.of(user));

        when(groupRepository.findById(group.getGroupId()))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> studentService.assignStudentToGroup(studentDTO));

        assertEquals("Group with " + studentDTO.getGroupDTO().getGroupId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(studentDTO.getUserId());
        verify(groupRepository).findById(studentDTO.getGroupDTO().getGroupId());

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"TEACHER", "ADMIN", "STUFF"})
    void assignStudentToGroup_shouldThrowException_whenUserIsNotStudent(Role role) {

        user.setRole(role);

        when(userRepository.findById(studentDTO.getUserId()))
                .thenReturn(Optional.of(user));

        when(groupRepository.findById(group.getGroupId()))
                .thenReturn(Optional.of(group));

        UserException exception = assertThrows(UserException.class, () -> studentService.assignStudentToGroup(studentDTO));

        assertEquals("User with " + studentDTO.getUserId() + " Id is not a student!", exception.getMessage());

        verify(userRepository).findById(studentDTO.getUserId());
        verify(groupRepository).findById(studentDTO.getGroupDTO().getGroupId());

    }

}
