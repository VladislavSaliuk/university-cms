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
        courseList.add(new Course("Test subject name 1", "description"));
        courseList.add(new Course("Test subject name 2", "description"));
        courseList.add(new Course("Test subject name 3", "description"));
        courseList.add(new Course("Test subject name 4", "description"));
        courseList.add(new Course("Test subject name 5", "description"));
    }

    @Test
    void getAll_shouldReturnCorrectSubjectList() {
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> actualCourseList = courseService.getAll();
        assertEquals(courseList, actualCourseList);
        verify(courseRepository).findAll();
    }

    @Test
    void getSubjectBySubjectName_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingName() {
        String subjectName = "Test subject name 1";
        Course expectedCourse = courseList.get(0);
        when(courseRepository.existsByCourseName(subjectName)).thenReturn(true);
        when(courseRepository.findCourseByCourseName(subjectName)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getSubjectBySubjectName(subjectName);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository).existsByCourseName(subjectName);
        verify(courseRepository).findCourseByCourseName(subjectName);
    }

    @Test
    void getSubjectBySubjectName_shouldReturnException_whenInputContainsSubjectWithNotExistingName() {
        String subjectName = "Test subject name ";
        when(courseRepository.existsByCourseName(subjectName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getSubjectBySubjectName(subjectName));
        assertEquals("Course name doesn't exists!",exception.getMessage());
        verify(courseRepository).existsByCourseName(subjectName);
        verify(courseRepository,never()).findCourseByCourseName(subjectName);
    }

    @Test
    void getSubjectBySubjectName_shouldReturnException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getSubjectBySubjectName(null));
        assertEquals("Input contains null!",exception.getMessage());
        verify(courseRepository,never()).existsByCourseName(null);
        verify(courseRepository,never()).findCourseByCourseName(null);
    }

    @Test
    void getSubjectBySubjectId_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingSubjectName() {
        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(subjectId -> {
                    Course course = new Course();
                    course.setCourseId(subjectId);
                    return course;
                })
                .collect(Collectors.toList());
        long subjectId = 1;
        Course expectedCourse = courseList.get(0);
        when(courseRepository.existsByCourseId(subjectId)).thenReturn(true);
        when(courseRepository.findCourseByCourseId(subjectId)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getSubjectBySubjectId(subjectId);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository).existsByCourseId(subjectId);
        verify(courseRepository).findCourseByCourseId(subjectId);
    }

    @Test
    void getSubjectBySubjectId_shouldThrowException_whenInputContainsSubjectWithNotExistingSubjectId() {
        long subjectId = 100;
        when(courseRepository.existsByCourseId(subjectId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.getSubjectBySubjectId(subjectId));
        assertEquals("Course Id doesn't exists!",exception.getMessage());
        verify(courseRepository).existsByCourseId(subjectId);
        verify(courseRepository,never()).findCourseByCourseId(subjectId);
    }


}
