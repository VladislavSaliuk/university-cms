package com.example.myschedule.repository;


import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_groups.sql", "/sql/insert_users.sql"})
public class UserRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    UserRepository userRepository;
    User user;
    @BeforeEach
    void setUp() {

        String name = "name";

        Group group = Group.builder()
                .name(name)
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

    }

    @Test
    void save_shouldPersistUser() {
        userRepository.save(user);
        assertEquals(11, userRepository.count());
    }

    @Test
    void save_shouldThrowException_whenUsernameIsNull() {
        user.setUsername(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @Test
    void save_shouldThrowException_whenPasswordIsNull() {
        user.setPassword(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @Test
    void save_shouldThrowException_whenEmailIsNull() {
        user.setEmail(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }
    @Test
    void save_shouldThrowException_whenRoleIsNull() {
        user.setRole(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @Test
    void save_shouldThrowException_whenStatusIsNull() {
        user.setStatus(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john_doe", "jane_smith", "mark_twain", "lucy_adams", "mike_jones",
            "susan_clark", "admin", "stuff", "emma_brown", "oliver_black"})
    void save_shouldThrowException_whenUsernameRepeats(String username) {
        user.setUsername(username);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john.doe@example.com", "jane.smith@example.com", "mark.twain@example.com",
            "lucy.adams@example.com", "mike.jones@example.com",
            "susan.clark@example.com", "salukvladislav81@gmail.com",
            "seattlemusic345@gmail.com", "emma.brown@example.com", "oliver.black@example.com"})
    void save_shouldThrowException_whenEmailRepeats(String email) {
        user.setEmail(email);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertEquals(10, userRepository.count());
    }

    @Test
    void findAll_shouldReturnUserList() {
        List<User> userList = userRepository.findAll();
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(10, userList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnUser(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        assertTrue(optionalUser.isPresent());
    }
    @Test
    void findById_shouldThrowException_whenUserNotFound() {
        long userId = 100L;
        Optional<User> optionalUser = userRepository.findById(userId);
        assertTrue(optionalUser.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john_doe", "jane_smith", "mark_twain", "lucy_adams", "mike_jones",
            "susan_clark", "admin", "stuff", "emma_brown", "oliver_black"})
    void findByUsername_shouldReturnUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        assertTrue(optionalUser.isPresent());
    }

    @Test
    void findByUsername_shouldThrowException_whenUserNotFound() {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        assertTrue(optionalUser.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldRemoveUser(long userId) {
        userRepository.deleteById(userId);
        assertEquals(9, userRepository.count());
    }
    @Test
    void deleteById_shouldNotRemoveUser_whenUserNotFound() {
        long userId = 100L;
        userRepository.deleteById(userId);
        assertEquals(10, userRepository.count());
    }

}