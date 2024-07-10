package com.example.universitycms.service;


import com.example.universitycms.model.*;
import com.example.universitycms.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserCourseServiceTest {


    @Autowired
    UserCourseService userCourseService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    UserCourseRepository userCourseRepository;


    @Test
    void assignUserOnCourse_shouldInsertUserCourseToDataBase_whenInputContainsExistingData() {

        long userId = 1;
        long courseId = 1;

        User user = new User();

        Course course = new Course();

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(false);

        when(userRepository.findUserByUserId(userId)).thenReturn(user);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        userCourseService.assignUserOnCourse(userId, courseId);

        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).save(any(UserCourse.class));
    }



    @Test
    void assignUserOnCourse_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;
        long courseId = 1;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("User with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }

    @Test
    void assignUserOnCourse_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long userId = 1;
        long courseId = 100;

        User user = new User();

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);
        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }

    @Test
    void assignUserOnCourse_shouldThrowException_whenCourseIsAlreadyAssigned() {
        long userId = 4;
        long courseId = 4;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId))
                .thenReturn(true)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("This user is already assigned on this course!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }


    @Test
    void removeUserFromCourse_shouldDeleteUserCourse_whenInputContainsExistingData() {

        long userId = 1;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        UserCourse userCourse = new UserCourse(user ,course);

        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(true);

        when(userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId))
                .thenReturn(userCourse);

        userCourseService.removeUserFromCourse(userId, courseId);

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository).delete(userCourse);
    }


    @Test
    void removeUserFromCourse_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("User with this Id doesn't exist!",exception.getMessage());
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }

    @Test
    void removeUserFromCourse_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long userId = 1;
        long courseId = 100;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.existsByUserId(userId))
                .thenReturn(true);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.removeUserFromCourse(userId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }

    @Test
    void removeUserFromCourse_shouldThrowException_whenCourseIsNotAssigned() {
        long userId = 1;
        long courseId = 2;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.existsByUserId(userId))
                .thenReturn(true);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(true);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userCourseService.removeUserFromCourse(userId, courseId));

        assertEquals("This user is not assigned on this course!", exception.getMessage());

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }


    @Test
    void getUnassignedCoursesForUse_shouldReturnCorrectCourseList_whenInputContainsExistingUserId() {

        long userId = 1;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        User user = new User();

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getUnassignedCoursesForUser(userId);

        assertEquals(9, unassignedCourses.size());
        assertEquals(3, unassignedCourses.get(2).getCourseId());

        verify(courseRepository).findAll();
        verify(userCourseRepository).findByUser_UserId(userId);
    }

    @Test
    void getUnassignedCoursesForUser_shouldReturnAllCourses_whenInputContainsNotExistingUserId() {

        long userId = 100;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        User user = new User();

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getUnassignedCoursesForUser(userId);

        assertEquals(9, unassignedCourses.size());

        verify(courseRepository).findAll();
        verify(userCourseRepository).findByUser_UserId(userId);
    }

    @Test
    void getAssignedCoursesForUser_shouldReturnEmptyCourseList_whenInputContainsExistingUserId() {

        long userId = 1;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        User user = new User();

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> assignedCourses = userCourseService.getAssignedCoursesForUser(userId);

        assertEquals(1, assignedCourses.size());

        verify(userCourseRepository).findByUser_UserId(userId);
    }

    @Test
    void getAssignedCoursesForUser_shouldReturnCorrectCourseList_whenInputContainsNotExistingUserId() {

        long userId = 100;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        User user = new User();

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getAssignedCoursesForUser(userId);

        assertEquals(1, unassignedCourses.size());

        verify(userCourseRepository).findByUser_UserId(userId);
    }

}
