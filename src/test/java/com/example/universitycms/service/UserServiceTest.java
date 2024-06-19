package com.example.universitycms.service;


import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import com.example.universitycms.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {


    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    static List<User> userList = new LinkedList<>();

    @BeforeAll
    static void init() {
        userList.add(new User("Test username 1", "Test password 1", "Test email 1", new Role("USER")));
        userList.add(new User("Test username 2", "Test password 2", "Test email 2", new Role("USER")));
        userList.add(new User("Test username 3", "Test password 3", "Test email 3", new Role("USER")));
        userList.add(new User("Test username 4", "Test password 4", "Test email 4", new Role("USER")));
        userList.add(new User("Test username 5", "Test password 5", "Test email 5", new Role("USER")));
    }
    @Test
    void registerUser_shouldInsertUserToDatabase_whenInputContainsUser() {
        User user = new User("Test username", "Test password", "Test email", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName()))
                .thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail()))
                .thenReturn(false);
        userService.registerUser(user);
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }
    @Test
    void registerUser_shouldThrowException_whenInputContainsUserWithExistingUserName() {
        User user = new User("Test username 1", "Test password", "Test email", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("This username is already exists!", exception.getMessage());
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository,never()).existsByEmail(user.getEmail());
        verify(userRepository,never()).save(user);
    }

    @Test
    void registerUser_shouldThrowException_whenInputContainsUserWithExistingEmail() {
        User user = new User("Test username", "Test password", "Test email 1", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName()))
                .thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("This E-mail is already exists!", exception.getMessage());
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void registerUser_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(userRepository, never()).existsByUserName(null);
        verify(userRepository, never()).existsByEmail(null);
        verify(userRepository, never()).save(null);
    }

    @Test
    void getAll_shouldReturnUserList() {
        when(userRepository.findAll())
                .thenReturn(userList);
        List<User> actualUserList = userService.getAll();
        assertEquals(5, actualUserList.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUsersByRoleId_shouldReturnUserWithCorrectRole_whenInputContainsExistingRoleId() {
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("USER");

        User user = new User();
        user.setRole(role);

        List<User> userList = Arrays.asList(user);

        when(userRepository.existsByRole_RoleId(role.getRoleId())).thenReturn(true);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> actualUserList = userService.getUsersByRole(role.getRoleId());
        assertEquals(userList, actualUserList);
        verify(userRepository).existsByRole_RoleId(role.getRoleId());
        verify(userRepository).findAll();
    }

    @Test
    void getUsersByRoleId_shouldThrowException_whenInputContainsExistingRoleId() {
        Role role = new Role();
        role.setRoleId(100);
        role.setRoleName("Test user");

        User user = new User();
        user.setRole(role);

        when(userRepository.existsByRole_RoleId(role.getRoleId()))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUsersByRole(role.getRoleId()));
        assertEquals(exception.getMessage(), "This role doesn't exist!");
        verify(userRepository).existsByRole_RoleId(role.getRoleId());
        verify(userRepository, never()).findAll();
    }
    @Test
    void getUserByUserId_shouldReturnCorrectUser_whenInputContainsExistingUserId() {
        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    return user;
                })
                .collect(Collectors.toList());
        long userId = 1;
        User expectedUser = new User();
        expectedUser.setUserId(userList.get(0).getUserId());
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findUserByUserId(userId)).thenReturn(expectedUser);
        User actualUser = userService.getUserByUserId(userId);
        assertTrue(actualUser.equals(expectedUser));
        verify(userRepository).existsByUserId(userId);
        verify(userRepository).findUserByUserId(userId);
    }

    @Test
    void getUserByUserId_shouldThrowException_whenInputContainsNotExistingUserId(){
        long userId = 100;
        when(userRepository.existsByUserId(userId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByUserId(userId));
        assertEquals("This user Id doesn't exist!", exception.getMessage());
        verify(userRepository).existsByUserId(userId);
        verify(userRepository,never()).findUserByUserId(userId);
    }

    @Test
    void getUserByEmail_shouldReturnUserWithCorrectEmail_whenInputContainsExistingEmail() {
        String email = "Test email 1";
        User expectedUser = new User();
        expectedUser.setEmail(userList.get(0).getEmail());
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findUserByEmail(email)).thenReturn(expectedUser);
        User actualUser = userService.getUserByEmail(email);
        assertTrue(actualUser.equals(expectedUser));
        verify(userRepository).existsByEmail(email);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void getUserByEmail_shouldThrowException_whenInputContainsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(userRepository,never()).existsByEmail(null);
        verify(userRepository,never()).findUserByEmail(null);
    }

    @Test
    void getUserByEmail_shouldThrowException_whenInputContainsNotExistingEmail(){
        String email = "Test email";
        when(userRepository.existsByEmail(email)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(email));
        assertEquals("This email doesn't exist!", exception.getMessage());
        verify(userRepository).existsByEmail(email);
        verify(userRepository,never()).findUserByEmail(email);
    }

}
