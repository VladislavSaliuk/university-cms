package com.example.myschedule.repository;


import com.example.myschedule.entity.Classroom;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_classrooms.sql"})
public class ClassroomRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    ClassroomRepository classroomRepository;
    Classroom classroom;

    @BeforeEach
    void setUp() {

        long number = 400L;

        classroom = Classroom.builder()
                .number(number)
                .build();
    }
    @Test
    void save_shouldPersistClassroom() {
        classroomRepository.save(classroom);
        assertEquals(11, classroomRepository.count());
    }
    @ParameterizedTest
    @ValueSource(longs = {101, 102, 103, 104, 105, 201, 202, 203, 301, 302})
    void save_shouldThrowException_whenNumberRepeats(long number) {
        classroom.setNumber(number);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> classroomRepository.save(classroom));
        assertEquals(10, classroomRepository.count());
    }

    @Test
    void findAll_shouldReturnClassroomList() {
        List<Classroom> classroomList = classroomRepository.findAll();
        assertNotNull(classroomList);
        assertFalse(classroomList.isEmpty());
        assertEquals(10, classroomList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnClassroom(long classRoomId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classRoomId);
        assertTrue(optionalClassroom.isPresent());
    }
    @Test
    void findById_shouldThrowException_whenClassroomNotFound() {
        long classRoomId = 100L;
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classRoomId);
        assertTrue(optionalClassroom.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {101, 102, 103, 104, 105, 201, 202, 203, 301, 302})
    void findByNumber_shouldReturnClassroom(long number) {
        Optional<Classroom> optionalClassroom = classroomRepository.findByNumber(number);
        assertTrue(optionalClassroom.isPresent());
    }

    @Test
    void findByNumber_shouldThrowException_whenClassroomNotFound() {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroom.getNumber());
        assertTrue(optionalClassroom.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldRemoveClassroom(long classRoomId) {
        classroomRepository.deleteById(classRoomId);
        assertEquals(9, classroomRepository.count());
    }

    @Test
    void deleteById_shouldNotRemoveClassroom_whenClassRoomNotFound() {
        long classRoomId = 100L;
        classroomRepository.deleteById(classRoomId);
        assertEquals(10, classroomRepository.count());
    }

}