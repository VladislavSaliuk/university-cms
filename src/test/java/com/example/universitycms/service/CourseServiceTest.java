package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @MockBean
    CourseRepository courseRepository;

    static List<Course> courseList = new LinkedList<>();

    @BeforeAll
    static void init() {
        courseList.add(new Course("Test course name 1", "description"));
        courseList.add(new Course("Test course name 2", "description"));
        courseList.add(new Course("Test course name 3", "description"));
        courseList.add(new Course("Test course name 4", "description"));
        courseList.add(new Course("Test course name 5", "description"));
    }

    @Test
    void getAll_shouldReturnCourseList() {
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> actualCourseList = courseService.getAll();
        assertEquals(courseList, actualCourseList);
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseByCourseName_shouldReturnCourse_whenInputContainsCourseWithExistingName() {
        String courseName = "Test course name 1";
        Course expectedCourse = courseList.get(0);
        when(courseRepository.existsByCourseName(courseName)).thenReturn(true);
        when(courseRepository.findCourseByCourseName(courseName)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getCourseByCourseName(courseName);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository).existsByCourseName(courseName);
        verify(courseRepository).findCourseByCourseName(courseName);
    }

    @Test
    void getCourseByCourseName_shouldReturnException_whenInputContainsCourseWithNotExistingName() {
        String courseName = "Test course name ";
        when(courseRepository.existsByCourseName(courseName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getCourseByCourseName(courseName));
        assertEquals("Course name doesn't exists!",exception.getMessage());
        verify(courseRepository).existsByCourseName(courseName);
        verify(courseRepository,never()).findCourseByCourseName(courseName);
    }

    @Test
    void getCourseByCourseName_shouldReturnException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getCourseByCourseName(null));
        assertEquals("Input contains null!",exception.getMessage());
        verify(courseRepository,never()).existsByCourseName(null);
        verify(courseRepository,never()).findCourseByCourseName(null);
    }

    @Test
    void getCourseByCourseId_shouldReturnCourse_whenInputContainsCourseWithExistingName() {
        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(subjectId -> {
                    Course course = new Course();
                    course.setCourseId(subjectId);
                    return course;
                })
                .collect(Collectors.toList());
        long courseId = 1;
        Course expectedCourse = courseList.get(0);
        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getCourseByCourseId(courseId);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository).existsByCourseId(courseId);
        verify(courseRepository).findCourseByCourseId(courseId);
    }

    @Test
    void getCourseByCourseId_shouldThrowException_whenInputContainsCourseWithNotExistingCourseId() {
        long courseId = 100;
        when(courseRepository.existsByCourseId(courseId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getCourseByCourseId(courseId));
        assertEquals("Course Id doesn't exists!",exception.getMessage());
        verify(courseRepository).existsByCourseId(courseId);
        verify(courseRepository,never()).findCourseByCourseId(courseId);
    }


}
