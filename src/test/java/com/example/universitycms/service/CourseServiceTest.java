package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    void createCourse_shouldInsertCourseToDatabase_whenCourseNameDoesNotExist() {
        Course course = new Course("Test course" , "Test description");
        when(courseRepository.existsByCourseName(course.getCourseName()))
                .thenReturn(false);
        courseService.createCourse(course);
        verify(courseRepository).existsByCourseName(course.getCourseName());
        verify(courseRepository).save(course);
    }

    @Test
    void createCourse_shouldThrowException_whenCourseNameExists() {
        Course course = new Course("Mathematics", "Test description");
        when(courseRepository.existsByCourseName(course.getCourseName()))
                .thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> {courseService.createCourse(course);});
        verify(courseRepository).existsByCourseName(course.getCourseName());
        verify(courseRepository, never()).save(course);
    }
    @Test
    void createCourse_shouldThrowException_whenCourseDoesNotContainName() {
        Course course = new Course();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(course));
        assertEquals(exception.getMessage(), "Course must contains name!");
        verify(courseRepository, never()).existsByCourseName(course.getCourseName());
        verify(courseRepository, never()).save(course);
    }

    @Test
    void removeCourseByCourseId_shouldRemoveCourse_whenInputContainsExistingCourseId() {
        long courseId = 1;
        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(true);
        courseService.removeCourseByCourseId(courseId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(courseRepository).deleteCourseByCourseId(courseId);
    }

    @Test
    void removeCourseByCourseId_shouldNotRemoveCourse_whenInputContainsNotExistingCourseId() {
        long courseId = 100;
        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.removeCourseByCourseId(courseId));
        assertEquals(exception.getMessage(),"Course with this id doesn't exist!");
        verify(courseRepository).existsByCourseId(courseId);
        verify(courseRepository,never()).deleteCourseByCourseId(courseId);
    }
    @Test
    void updateCourse_shouldUpdateCourse_whenInputContainsExistingCourse() {
        long courseId = 1;

        Course existingCourse = new Course();
        existingCourse.setCourseId(courseId);
        existingCourse.setCourseName("Mathematics");
        existingCourse.setCourseDescription("Study of numbers, quantities, shapes, and patterns.");

        Course updatedCourse = new Course();
        updatedCourse.setCourseId(courseId);
        updatedCourse.setCourseName("Test course name");
        updatedCourse.setCourseDescription("Test description");

        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(existingCourse);
        when(courseRepository.existsByCourseName(updatedCourse.getCourseName())).thenReturn(false);

        courseService.updateCourse(updatedCourse);

        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository).existsByCourseName(updatedCourse.getCourseName());
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void updateCourse_shouldThrowException_whenInputContainsNotExistingCourse() {
        long courseId = 100;

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(null)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(course));
        assertEquals(exception.getMessage(), "This course doesn't exist!");

        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository,never()).existsByCourseName(course.getCourseName());
        verify(courseRepository,never()).save(course);
    }

    @Test
    void updateCourse_shouldThrowException_whenUpdatedCourseNameContainsNull() {
        long courseId = 1;

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Mathematics");
        course.setCourseDescription("Study of numbers, quantities, shapes, and patterns.");

        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        course.setCourseName(null);
        course.setCourseDescription(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(course));

        assertEquals(exception.getMessage(), "Course must contains name!");
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository, never()).existsByCourseName(course.getCourseName());
        verify(courseRepository, never()).save(course);
    }

    @Test
    void updateCourse_shouldThrowException_whenUpdatedCourseContainsExistingCourseName() {
        long courseId = 1;

        Course existingCourse = new Course();
        existingCourse.setCourseId(courseId);
        existingCourse.setCourseName("Mathematics");
        existingCourse.setCourseDescription("Study of numbers, quantities, shapes, and patterns.");

        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(existingCourse);

        Course updatedCourse = new Course();
        updatedCourse.setCourseId(courseId);
        updatedCourse.setCourseName("Biology");
        updatedCourse.setCourseDescription("Study of living organisms and their interactions.");

        when(courseRepository.existsByCourseName(updatedCourse.getCourseName())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(updatedCourse));

        assertEquals("Course with this name already exists!", exception.getMessage());
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository).existsByCourseName(updatedCourse.getCourseName());
        verify(courseRepository, never()).save(updatedCourse);
    }


    @Test
    void getAll_shouldReturnCourseList() {
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> actualCourseList = courseService.getAll();
        assertEquals(courseList, actualCourseList);
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseByCourseId_shouldReturnCourse_whenInputContainsCourseWithExistingName() {
        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
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
        assertEquals("Course with this Id doesn't exists!",exception.getMessage());
        verify(courseRepository).existsByCourseId(courseId);
        verify(courseRepository,never()).findCourseByCourseId(courseId);
    }


}
