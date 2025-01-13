package com.example.myschedule.repository;

import com.example.myschedule.model.Course;
import com.example.myschedule.model.DayOfWeek;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void save_shouldInsertCourseToDatabase() {
        Course course = new Course("Test course name", "Test course description");
        courseRepository.save(course);
        assertEquals(11, courseRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void deleteCourseByCourseId_shouldDeleteCourse_whenCourseIdExists() {
        long courseId = 1;
        courseRepository.deleteCourseByCourseId(courseId);
        assertEquals(9, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void deleteCourseByCourseId_shouldNotDeleteCourse_whenCourseIdDoesNotExist() {
        long courseId = 1000;
        courseRepository.deleteCourseByCourseId(courseId);
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findAll_shouldReturnCourseList() {
        List<Course> courseList = courseRepository.findAll();
        assertEquals(10, courseList.size());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findCourseByCourseId_shouldReturnCourse_whenInputContainsExistingCourseId() {
        long courseId = 6;
        Course course = courseRepository.findCourseByCourseId(courseId);
        assertTrue(course.getCourseId() == courseId);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findCourseByCourseId_shouldReturnNull_whenInputContainsNotExistingCourseId() {
        long courseId = 100;
        Course course = courseRepository.findCourseByCourseId(courseId);
        assertNull(course);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void existsByCourseName_shouldReturnTrue_whenInputContainsExistingCourseName() {
        String courseName = "Computer Science";
        boolean isCourseNameExists = courseRepository.existsByCourseName(courseName);
        assertTrue(isCourseNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void existsByCourseName_shouldReturnFalse_whenInputContainsNotExistingCourseName() {
        String courseName = "Test course name";
        boolean isCourseNameExists = courseRepository.existsByCourseName(courseName);
        assertFalse(isCourseNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void existsByCourseName_shouldReturnFalse_whenInputContainsNull() {
        boolean isCourseNameExists = courseRepository.existsByCourseName(null);
        assertFalse(isCourseNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void existsByCourseId_shouldReturnTrue_whenInputContainsExistingCourseId() {
        long courseId = 4;
        boolean isCourseIdExists = courseRepository.existsByCourseId(courseId);
        assertTrue(isCourseIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void existsByCourseId_shouldReturnFalse_whenInputContainsNotExistingCourseId() {
        long courseId = 404;
        boolean isCourseIdExists = courseRepository.existsByCourseId(courseId);
        assertFalse(isCourseIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findAllByDayOfWeek_shouldReturnCorrectCourseList_whenInputContainsCorrectDayOfWeek() {
        List<Course> courseList = courseRepository.findAllByDayOfWeek(DayOfWeek.MONDAY.getValue());
        assertNotNull(courseList);
        assertEquals(2, courseList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findAllByDayOfWeek_shouldReturnEmptyList_whenInputContainsIncorrectDayOfWeek() {
        String dayOfWeek = "Test day of week";
        List<Course> courseList = courseRepository.findAllByDayOfWeek(dayOfWeek);
        assertEquals(Collections.emptyList(), courseList);
        assertEquals(0, courseList.size());
    }



}
