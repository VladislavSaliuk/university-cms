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
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test course description");

        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);

        when(userRepository.findUserByUserId(userId)).thenReturn(user);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(userCourseRepository.existsByUserAndCourse(user, course)).thenReturn(false);

        ArgumentCaptor<UserCourse> userCourseCaptor = ArgumentCaptor.forClass(UserCourse.class);

        userCourseService.assignUserOnCourse(userId, courseId);

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUserAndCourse(user, course);
        verify(userCourseRepository).save(userCourseCaptor.capture());

        UserCourse capturedUserCourse = userCourseCaptor.getValue();
        assertEquals(user, capturedUserCourse.getUser());
        assertEquals(course, capturedUserCourse.getCourse());
    }



    @Test
    void assignUserOnCourse_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.existsByUserId(userId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("User with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).existsByUserId(userId);
        verify(courseRepository, never()).existsByCourseId(courseId);
        verify(userRepository, never()).findUserByUserId(userId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUserAndCourse(user, course);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }

    @Test
    void assignUserOnCourse_shouldThrowException_whenInputContainsNotExistingCourseId() {

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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userRepository, never()).findUserByUserId(userId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUserAndCourse(user, course);
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


        when(userRepository.existsByUserId(userId))
                .thenReturn(true);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(true);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(userCourseRepository.existsByUserAndCourse(user, course))
                .thenReturn(true)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("This user is already assigned on this course!", exception.getMessage());

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUserAndCourse(user, course);
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

        when(userRepository.findUserByUserId(userId)).thenReturn(user);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(userCourseRepository.existsByUserAndCourse(user, course)).thenReturn(true);

        when(userCourseRepository.findByUserAndCourse(user, course))
                .thenReturn(userCourse);

        userCourseService.removeUserFromCourse(userId, courseId);

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUserAndCourse(user, course);
        verify(userCourseRepository).findByUserAndCourse(user, course);
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

        when(userRepository.existsByUserId(userId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignUserOnCourse(userId, courseId));

        assertEquals("User with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).existsByUserId(userId);
        verify(courseRepository, never()).existsByCourseId(courseId);
        verify(userRepository, never()).findUserByUserId(userId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUserAndCourse(user, course);
        verify(userCourseRepository, never()).findByUserAndCourse(user, course);
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
        verify(userRepository, never()).findUserByUserId(userId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUserAndCourse(user, course);
        verify(userCourseRepository, never()).findByUserAndCourse(user, course);
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

        when(userCourseRepository.existsByUserAndCourse(user, course))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userCourseService.removeUserFromCourse(userId, courseId));

        assertEquals("This user is not assigned on this course!", exception.getMessage());

        verify(userRepository).existsByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUserAndCourse(user, course);
        verify(userCourseRepository, never()).findByUserAndCourse(user, course);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }


    @Test
    void getUnassignedCoursesForUse_shouldReturnCorrectCourseList_whenInputContainsExistingUserId() {

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
        when(userCourseRepository.findAll()).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getUnassignedCoursesForUser(1);

        assertEquals(10, unassignedCourses.size());
        assertEquals(2, unassignedCourses.get(2).getCourseId());

        verify(courseRepository).findAll();
        verify(userCourseRepository).findAll();
    }

    @Test
    void getUnassignedCoursesForUser_shouldReturnAllCourses_whenInputContainsNotExistingUserId() {

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
        when(userCourseRepository.findAll()).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getUnassignedCoursesForUser(100);

        assertEquals(10, unassignedCourses.size());

        verify(courseRepository).findAll();
        verify(userCourseRepository).findAll();
    }

    @Test
    void getAssignedCoursesForUser_shouldReturnEmptyCourseList_whenInputContainsExistingUserId() {

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        User user = new User();

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(userCourseRepository.findAll()).thenReturn(Collections.singletonList(userCourse));

        List<Course> assignedCourses = userCourseService.getAssignedCoursesForUser(1);

        assertEquals(0, assignedCourses.size());

        verify(userCourseRepository).findAll();
    }

    @Test
    void getAssignedCoursesForUser_shouldReturnCorrectCourseList_whenInputContainsNotExistingUserId() {

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
        when(userCourseRepository.findAll()).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getAssignedCoursesForUser(100);

        assertEquals(0, unassignedCourses.size());

        verify(userCourseRepository).findAll();
    }

}
