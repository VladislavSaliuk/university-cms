package com.example.myschedule.repository;


import com.example.myschedule.entity.Classroom;
import com.example.myschedule.entity.Course;
import com.example.myschedule.entity.DayOfWeek;
import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Lesson;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import org.junit.Assert;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_classrooms.sql", "/sql/insert_groups.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_lessons.sql"})
public class LessonRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    LessonRepository lessonRepository;
    Lesson lesson;

    @BeforeEach
    void setUp() {

        String groupName = "Group name";

        Group group = Group.builder()
                .groupName(groupName)
                .build();

        String courseName = "Course name";
        String courseDescription = "Course description";

        Course course = Course.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        long classroomNumber = 400L;
        String classroomDescription = "Classroom description";

        Classroom classroom = Classroom.builder()
                .classroomNumber(classroomNumber)
                .classroomDescription(classroomDescription)
                .build();

        LocalTime startTime = LocalTime.of(13,00);
        LocalTime endTime = LocalTime.of(13, 30);

        lesson = Lesson.builder()
                .course(course)
                .group(group)
                .startTime(startTime)
                .endTime(endTime)
                .dayOfWeek(DayOfWeek.MONDAY)
                .classroom(classroom)
                .build();

    }

    @Test
    void save_shouldPersistLesson() {
        lessonRepository.save(lesson);
        Assert.assertEquals(11, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenCourseIsNull() {
        lesson.setCourse(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenGroupIsNull() {
        lesson.setGroup(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenStartTimeIsNull() {
        lesson.setStartTime(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenEndTimeIsNull() {
        lesson.setEndTime(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenDayOfWeekIsNull() {
        lesson.setDayOfWeek(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void save_shouldThrowException_whenClassroomIsNull() {
        lesson.setClassroom(null);
        DataIntegrityViolationException exception = Assert.assertThrows(DataIntegrityViolationException.class, () -> lessonRepository.save(lesson));
        Assert.assertEquals(10, lessonRepository.count());
    }

    @Test
    void findAll_shouldReturnLessonList() {
        List<Lesson> lessonList = lessonRepository.findAll();
        Assert.assertNotNull(lessonList);
        assertFalse(lessonList.isEmpty());
        Assert.assertEquals(10, lessonList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnLesson(long lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        Assert.assertTrue(optionalLesson.isPresent());
    }
    @Test
    void findById_shouldThrowException_whenLessonNotFound() {
        long lessonId = 100L;
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        Assert.assertTrue(optionalLesson.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldRemoveLesson(long lessonId) {
        lessonRepository.deleteById(lessonId);
        Assert.assertEquals(9, lessonRepository.count());
    }
    @Test
    void deleteById_shouldNotRemoveLesson_whenLessonNotFound() {
        long lessonId = 100L;
        lessonRepository.deleteById(lessonId);
        Assert.assertEquals(10, lessonRepository.count());
    }

}