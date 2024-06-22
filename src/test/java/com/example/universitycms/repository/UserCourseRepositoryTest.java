package com.example.universitycms.repository;


import com.example.universitycms.model.Course;
import com.example.universitycms.model.User;
import com.example.universitycms.model.UserCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserCourseRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserCourseRepository userCourseRepository;


    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void save_shouldInsertUserCourseToDatabase_whenUserCourseDoesNotExist() {
        long userId = 1;
        long courseId = 3;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);
        assertEquals(11, userCourseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void save_shouldNotInsertUserCourseToDatabase_whenUserCourseExists() {
        long userId = 1;
        long courseId = 1;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);
        assertEquals(10, userCourseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void delete_shouldDeleteUserCourse_whenInputContainsExistingUserCourse() {
        long userId = 1;
        long courseId = 1;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.delete(userCourse);
        assertEquals(9, userCourseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void delete_shouldNotDeleteUserCourse_whenInputContainsNotExistingUserCourse() {
        long userId = 1;
        long courseId = 10;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.delete(userCourse);
        assertEquals(10, userCourseRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUserAndCourse_shouldReturnUserCourse_whenInputContainsExistingData() {
        long userId = 1;
        long courseId = 1;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course);
        assertNotNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUserAndCourse_shouldReturnNull_whenInputContainsNotExistingUser() {
        long userId = 100;
        long courseId = 1;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUserAndCourse_shouldReturnNull_whenInputContainsNotExistingCourse() {
        long userId = 1;
        long courseId = 100;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUserAndCourse_shouldReturnNull_whenInputDoesNotContainUser() {
        long courseId = 1;
        Course course = courseRepository.findCourseByCourseId(courseId);
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(null, course);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUserAndCourse_shouldReturnNull_whenInputDoesNotContainCourse() {
        long userId = 1;
        User user = userRepository.findUserByUserId(userId);
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, null);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUserAndCourse_shouldReturnTrue_whenInputContainsExistingData() {
        long userId = 1;
        long courseId = 1;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isUserCourseExists = userCourseRepository.existsByUserAndCourse(user, course);
        assertTrue(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUserAndCourse_shouldReturnFalse_whenInputContainsNotExistingUser() {
        long userId = 100;
        long courseId = 1;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isUserCourseExists = userCourseRepository.existsByUserAndCourse(user, course);
        assertFalse(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUserAndCourse_shouldReturnFalse_whenInputContainsNotExistingCourse() {
        long userId = 1;
        long courseId = 100;
        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isUserCourseExists = userCourseRepository.existsByUserAndCourse(user, course);
        assertFalse(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUserAndCourse_shouldReturnFalse_whenInputDoesNotContainUser() {
        long courseId = 1;
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isUserCourseExists = userCourseRepository.existsByUserAndCourse(null, course);
        assertFalse(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql", "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUserAndCourse_shouldReturnFalse_whenInputDoesNotContainCourse() {
        long userId = 1;
        User user = userRepository.findUserByUserId(userId);
        boolean isUserCourseExists = userCourseRepository.existsByUserAndCourse(user, null);
        assertFalse(isUserCourseExists);
    }


}
