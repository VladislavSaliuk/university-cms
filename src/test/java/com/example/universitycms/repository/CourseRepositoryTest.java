package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

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
    void deleteAll_shouldRemoveAllCourses() {
        courseRepository.deleteAll();
        assertEquals(0, courseRepository.count());
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
    void deleteCourseByCourseName_shouldDeleteCourse_whenCourseNameExists() {
        String courseName = "Computer Science";
        courseRepository.deleteCourseByCourseName(courseName);
        assertEquals(9, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void deleteCourseByCourseName_shouldNotDeleteCourse_whenCourseNameDoesNotExist() {
        String courseName = "Test course name";
        courseRepository.deleteCourseByCourseName(courseName);
        assertEquals(10, courseRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void deleteCourseByCourseName_shouldNotDeleteCourse_whenInputContainsNull() {
        courseRepository.deleteCourseByCourseName(null);
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
    void findCourseByCourseName_shouldReturnCourse_whenInputContainsExistingCourseName() {
        String courseName = "Computer Science";
        Course course = courseRepository.findCourseByCourseName(courseName);
        assertEquals(course.getCourseName(), courseName);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findCourseByCourseName_shouldReturnNull_whenInputContainsNotExistingCourseName() {
        String courseName = "Test course name";
        Course course = courseRepository.findCourseByCourseName(courseName);
        assertNull(course);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_courses.sql"})
    void findCourseByCourseName_shouldReturnNull_whenInputContainsNull() {
        Course course = courseRepository.findCourseByCourseName(null);
        assertNull(course);
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
}
