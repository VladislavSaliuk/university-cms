package com.example.myschedule.repository;

import com.example.myschedule.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_groups.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql"})
public class CourseRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    CourseRepository courseRepository;
    Course course;
    @BeforeEach
    void setUp() {

        String username = "username";
        String password = "$2y$10$fGkGf4h1aCSlfPBpwTfvGerNI4puZTLVQeZLLCpSrqh8WcPuMtr7a";
        String email = "email@gmail.com";
        String firstname = "Firstname";
        String lastname = "Lastname";

        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.TEACHER)
                .status(Status.ACTIVE)
                .build();

        String description = "Description";

        course = Course.builder()
                .user(user)
                .description(description)
                .build();

    }
    @Test
    void save_shouldPersistCourse() {
        courseRepository.save(course);
        assertEquals(11, courseRepository.count());
    }

    @Test
    void findAll_shouldReturnCourseList() {
        List<Course> courseList = courseRepository.findAll();
        assertNotNull(courseList);
        assertFalse(courseList.isEmpty());
        assertEquals(10, courseList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnCourse(long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        assertTrue(optionalCourse.isPresent());
    }
    @Test
    void findById_shouldThrowException_whenCourseNotFound() {
        long courseId = 100L;
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        assertTrue(optionalCourse.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldRemoveCourse(long courseId) {
        courseRepository.deleteById(courseId);
        assertEquals(9, courseRepository.count());
    }
    @Test
    void deleteById_shouldNotRemoveCourse_whenCourseNotFound() {
        long courseId = 100L;
        courseRepository.deleteById(courseId);
        assertEquals(10, courseRepository.count());
    }

}
