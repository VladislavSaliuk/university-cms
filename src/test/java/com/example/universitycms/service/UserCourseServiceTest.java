package com.example.universitycms.service;


import com.example.universitycms.model.*;
import com.example.universitycms.repository.*;
import org.junit.jupiter.api.Test;
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
    void assignTeacherOnCourse_shouldInsertUserCourseToDataBase_whenInputContainsExistingData() {

        long userId = 1;
        long courseId = 1;

        User user = new User();
        user.setRole(RoleId.TEACHER.getRoleId());

        Course course = new Course();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(false);

        userCourseService.assignTeacherOnCourse(userId, courseId);

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository).save(any(UserCourse.class));
    }



    @Test
    void assignTeacherOnCourse_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;
        long courseId = 1;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("Teacher with this Id doesn't exist!",exception.getMessage());
        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);

        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }

    @Test
    void assignTeacherOnCourse_shouldThrowException_whenInputContainsUserIdNotBelongTeacher() {

        long userId = 1;
        long courseId = 100;

        User user = new User();
        user.setRole(RoleId.STUDENT.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("This user is not a teacher!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }
    @Test
    void assignTeacherOnCourse_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long userId = 1;
        long courseId = 100;

        User user = new User();
        user.setRole(RoleId.TEACHER.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }
    @Test
    void assignTeacherOnCourse_shouldThrowException_whenCourseIsAlreadyAssigned() {
        long userId = 4;
        long courseId = 4;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

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
                userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("This teacher is already assigned on this course!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).save(any(UserCourse.class));
    }


    @Test
    void removeTeacherFromCourse_shouldDeleteUserCourse_whenInputContainsExistingData() {

        long userId = 1;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        Course course = new Course();
        course.setCourseId(courseId);

        UserCourse userCourse = new UserCourse(user ,course);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(true);

        when(userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId))
                .thenReturn(userCourse);

        userCourseService.removeTeacherFromCourse(userId, courseId);

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository).delete(userCourse);
    }


    @Test
    void removeTeacherFromCourse_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("Teacher with this Id doesn't exist!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository,never()).existsByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }

    @Test
    void removeTeacherFromCourse_shouldThrowException_whenInputContainsUserIdNotBelongTeacher() {

        long userId = 100;
        long courseId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.STUDENT.getRoleId());

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.assignTeacherOnCourse(userId, courseId));

        assertEquals("This user is not a teacher!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository,never()).existsByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }

    @Test
    void removeTeacherFromCourse_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long userId = 1;
        long courseId = 100;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.removeTeacherFromCourse(userId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository, never()).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }

    @Test
    void removeTeacherFromCourse_shouldThrowException_whenCourseIsNotAssigned() {
        long userId = 1;
        long courseId = 2;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(true);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userCourseService.removeTeacherFromCourse(userId, courseId));

        assertEquals("This teacher is not assigned on this course!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(userCourseRepository).existsByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).findByUser_UserIdAndCourse_CourseId(userId, courseId);
        verify(userCourseRepository, never()).delete(any(UserCourse.class));
    }


    @Test
    void getUnassignedCoursesForUse_shouldReturnCorrectCourseList_whenInputContainsExistingUserId() {

        long userId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());


        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> unassignedCourses = userCourseService.getUnassignedCoursesForTeacher(userId);

        assertEquals(9, unassignedCourses.size());
        assertEquals(3, unassignedCourses.get(2).getCourseId());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository).findAll();
        verify(userCourseRepository).findByUser_UserId(userId);
    }

    @Test
    void getUnassignedCoursesForUse_shouldThrowException_whenInputContainsUserIdDoesNotExist() {

        long userId = 100;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.getUnassignedCoursesForTeacher(userId));

        assertEquals("Teacher with this Id doesn't exist!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository,never()).findAll();
        verify(userCourseRepository,never()).findByUser_UserId(userId);
    }
    @Test
    void getUnassignedCoursesForUse_shouldThrowException_whenInputContainsUserIdNotBelongTeacher() {

        long userId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.STUDENT.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.getUnassignedCoursesForTeacher(userId));

        assertEquals("This user is not a teacher!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(courseRepository,never()).findAll();
        verify(userCourseRepository,never()).findByUser_UserId(userId);
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
        user.setUserId(userId);
        user.setRole(RoleId.TEACHER.getRoleId());

        UserCourse userCourse = new UserCourse(user, courseList.get(2));

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        when(userCourseRepository.findByUser_UserId(userId)).thenReturn(Collections.singletonList(userCourse));

        List<Course> assignedCourses = userCourseService.getAssignedCoursesForTeacher(userId);

        assertEquals(1, assignedCourses.size());

        verify(userRepository).findUserByUserId(userId);
        verify(userCourseRepository).findByUser_UserId(userId);
    }

    @Test
    void getAssignedCoursesForUser_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.STUDENT.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.getAssignedCoursesForTeacher(userId));

        assertEquals("This user is not a teacher!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(userCourseRepository,never()).findByUser_UserId(userId);
    }
    @Test
    void getAssignedCoursesForUser_shouldThrowException_whenInputContainsUserIdNotBelongTeacher() {

        long userId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setRole(RoleId.STUDENT.getRoleId());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userCourseService.getAssignedCoursesForTeacher(userId));

        assertEquals("This user is not a teacher!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(userCourseRepository,never()).findByUser_UserId(userId);
    }

}
