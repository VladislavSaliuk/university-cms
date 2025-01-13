package com.example.myschedule.repository;


import com.example.myschedule.model.Course;
import com.example.myschedule.model.User;
import com.example.myschedule.model.UserCourse;
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
public class UserCourseRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserCourseRepository userCourseRepository;


    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void save_shouldInsertUserCourseToDatabase_whenUserCourseDoesNotExist() {
        long userId = 1;
        long courseId = 3;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);

        assertEquals(11, userCourseRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void save_shouldNotInsertUserCourseToDatabase_whenUserCourseExists() {
        long userId = 1;
        long courseId = 1;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);

        assertEquals(10, userCourseRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void delete_shouldDeleteUserCourse_whenInputContainsExistingUserCourse() {
        long userId = 1;
        long courseId = 1;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.delete(userCourse);

        assertEquals(9, userCourseRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void delete_shouldNotDeleteUserCourse_whenInputContainsNotExistingUserCourse() {
        long userId = 1;
        long courseId = 10;

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.delete(userCourse);

        assertEquals(10, userCourseRepository.count());
        assertEquals(10, userRepository.count());
        assertEquals(10, courseRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUser_UserIdAndCourse_CourseId_shouldReturnUserCourse_whenInputContainsExistingData() {
        long userId = 1;
        long courseId = 1;
        UserCourse userCourse = userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertNotNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUser_UserIdAndCourse_CourseId_shouldReturnNull_whenInputContainsNotExistingUserId() {
        long userId = 100;
        long courseId = 1;
        UserCourse userCourse = userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUser_UserIdAndCourse_CourseId_shouldReturnNull_whenInputContainsNotExistingCourseId() {
        long userId = 1;
        long courseId = 100;
        UserCourse userCourse = userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertNull(userCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUser_UserIdAndCourse_CourseId_shouldReturnTrue_whenInputContainsExistingData() {
        long userId = 1;
        long courseId = 1;
        boolean isUserCourseExists = userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertTrue(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUser_UserIdAndCourse_CourseId_shouldReturnFalse_whenInputContainsNotExistingUserId() {
        long userId = 100;
        long courseId = 1;
        boolean isUserCourseExists = userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertFalse(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void existsByUser_UserIdAndCourse_CourseId_shouldReturnFalse_whenInputContainsNotExistingCourseId() {
        long userId = 1;
        long courseId = 100;
        boolean isUserCourseExists = userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        assertFalse(isUserCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUser_UserId_shouldReturnList_whenInputContainsExistingUserId() {
        long userId = 4;
        List<UserCourse> userCourseList = userCourseRepository.findByUser_UserId(userId);
        assertNotEquals(Collections.emptyList(),userCourseList);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_roles.sql","/sql/insert_user_statuses.sql",  "/sql/insert_users.sql", "/sql/insert_courses.sql", "/sql/insert_users_courses.sql"})
    void findByUser_UserId_shouldReturnEmptyList_whenInputContainsNotExistingUserId() {
        long userId = 100;
        List<UserCourse> userCourseList = userCourseRepository.findByUser_UserId(userId);
        assertEquals(Collections.emptyList(),userCourseList);
    }

}
