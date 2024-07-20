package com.example.universitycms.service;

import com.example.universitycms.exception.CourseNameException;
import com.example.universitycms.exception.CourseNotFoundException;
import com.example.universitycms.exception.ScheduleTimeException;
import com.example.universitycms.model.Course;
import com.example.universitycms.model.DayOfWeek;
import com.example.universitycms.repository.CourseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
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
        courseList.add(new Course("Test course name 1", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("08:30"), LocalTime.parse("10:00")));
        courseList.add(new Course("Test course name 2", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("10:30"), LocalTime.parse("12:00")));
        courseList.add(new Course("Test course name 3", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("12:30"), LocalTime.parse("14:00")));
        courseList.add(new Course("Test course name 4", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("14:30"), LocalTime.parse("16:00")));
        courseList.add(new Course("Test course name 5", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("16:30"), LocalTime.parse("18:00")));
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
        CourseNameException exception = assertThrows(CourseNameException.class, () -> {courseService.createCourse(course);});
        assertEquals("Course with this name already exists!", exception.getMessage());
        verify(courseRepository).existsByCourseName(course.getCourseName());
        verify(courseRepository, never()).save(course);
    }
    @Test
    void createCourse_shouldThrowException_whenCourseDoesNotContainName() {
        Course course = new Course();
        CourseNameException exception = assertThrows(CourseNameException.class, () -> courseService.createCourse(course));
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
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.removeCourseByCourseId(courseId));
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
        updatedCourse.setCourseDescription("Test course description");

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

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(course));
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

        CourseNameException exception = assertThrows(CourseNameException.class, () -> courseService.updateCourse(course));

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

        CourseNameException exception = assertThrows(CourseNameException.class, () -> courseService.updateCourse(updatedCourse));

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
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(expectedCourse);
        Course actualCourse = courseService.getCourseByCourseId(courseId);
        assertEquals(expectedCourse, actualCourse);
        verify(courseRepository).findCourseByCourseId(courseId);
    }

    @Test
    void getCourseByCourseId_shouldThrowException_whenInputContainsCourseWithNotExistingCourseId() {
        long courseId = 100;

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getCourseByCourseId(courseId));
        assertEquals("Course with this Id doesn't exists!",exception.getMessage());
        verify(courseRepository).findCourseByCourseId(courseId);
    }


    @Test
    void isTimeAvailableForCourse_shouldReturnFalse_whenStartCourseTimeIsBiggerThanEnd() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = CourseService.class.getDeclaredMethod("isTimeAvailableForCourse", Course.class);
        method.setAccessible(true);

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test course description");
        course.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("14:00");
        LocalTime endCourseTime = LocalTime.parse("12:00");

        course.setStartCourseTime(startCourseTime);
        course.setEndCourseTime(endCourseTime);

        boolean isTimeAvailableForCourse= (Boolean) method.invoke(courseService,course);

        assertFalse(isTimeAvailableForCourse);
        verify(courseRepository, never()).findAllByDayOfWeek(course.getDayOfWeek());
    }


    @Test
    void isTimeAvailableForCourse_shouldReturnFalse_shouldReturnTrue_whenDayOfWeekDoesHaveAnyCourses() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = CourseService.class.getDeclaredMethod("isTimeAvailableForCourse", Course.class);
        method.setAccessible(true);

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test course description");
        course.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("08:30");
        LocalTime endCourseTime = LocalTime.parse("10:00");

        course.setStartCourseTime(startCourseTime);
        course.setEndCourseTime(endCourseTime);

        when(courseRepository.findAllByDayOfWeek(course.getDayOfWeek()))
                .thenReturn(Collections.emptyList());

        boolean isTimeAvailableForCourse= (Boolean) method.invoke(courseService,course);

        assertTrue(isTimeAvailableForCourse);
        verify(courseRepository).findAllByDayOfWeek(course.getDayOfWeek());

    }

    @Test
    void isTimeAvailableForCourse_shouldReturnFalse_whenCourseInterspersedWithAnotherDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = CourseService.class.getDeclaredMethod("isTimeAvailableForCourse", Course.class);
        method.setAccessible(true);

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test course description");
        course.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("09:00");
        LocalTime endCourseTime = LocalTime.parse("11:30");

        course.setStartCourseTime(startCourseTime);
        course.setEndCourseTime(endCourseTime);

        when(courseRepository.findAllByDayOfWeek(course.getDayOfWeek()))
                .thenReturn(courseList);

        boolean isTimeAvailableForCourse= (Boolean) method.invoke(courseService,course);

        assertFalse(isTimeAvailableForCourse);
        verify(courseRepository).findAllByDayOfWeek(course.getDayOfWeek());

    }

    @Test
    void isTimeAvailableForCourse_shouldReturnTrue_whenCourseIsNotInterspersedWithAnotherDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = CourseService.class.getDeclaredMethod("isTimeAvailableForCourse", Course.class);
        method.setAccessible(true);

        Course course = new Course();

        course.setCourseId(1);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test course description");
        course.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("18:30");
        LocalTime endCourseTime = LocalTime.parse("20:00");

        course.setStartCourseTime(startCourseTime);
        course.setEndCourseTime(endCourseTime);

        when(courseRepository.findAllByDayOfWeek(course.getDayOfWeek()))
                .thenReturn(courseList);

        boolean isTimeAvailableForCourse= (Boolean) method.invoke(courseService,course);

        assertTrue(isTimeAvailableForCourse);
        verify(courseRepository).findAllByDayOfWeek(course.getDayOfWeek());

    }

    @Test
    void setScheduleTimeForCourse_shouldThrowException_whenInputContainsNotExistingCourse() {

        Course course = new Course();
        course.setCourseId(100);
        course.setCourseName("Test course name");

        when(courseRepository.findCourseByCourseId(course.getCourseId()))
                .thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.setScheduleTimeForCourse(course));

        assertEquals("This course doesn't exist!", exception.getMessage());

        verify(courseRepository).findCourseByCourseId(course.getCourseId());
        verify(courseRepository, never()).save(course);
    }

    @Test
    void setScheduleTimeForCourse_shouldThrowException_whenInputContainsCourseWithOutDayOfWeek() {

        long courseId = 1;

        Course existingCourse = new Course();
        existingCourse.setCourseId(courseId);
        existingCourse.setCourseName("Test course name");
        existingCourse.setCourseDescription("Test course description");

        Course updatedCourse = new Course();
        updatedCourse.setCourseId(courseId);
        updatedCourse.setCourseName("Test course name");
        updatedCourse.setCourseDescription("Test course description");

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(existingCourse);

        ScheduleTimeException exception = assertThrows(ScheduleTimeException.class, () -> courseService.setScheduleTimeForCourse(updatedCourse));

        assertEquals("Day of week is not set!" , exception.getMessage());

        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository,never()).save(updatedCourse);
    }

    @Test
    void setScheduleTimeForCourse_shouldThrowException_whenCourseTimeIsNotAvailable() {

        long courseId = 1;

        Course existingCourse = new Course();
        existingCourse.setCourseId(courseId);
        existingCourse.setCourseName("Test course name");
        existingCourse.setCourseDescription("Test course description");

        Course updatedCourse = new Course();
        updatedCourse.setCourseId(courseId);
        updatedCourse.setCourseName("Test course name");
        updatedCourse.setCourseDescription("Test course description");
        updatedCourse.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("10:20");
        LocalTime endCourseTime = LocalTime.parse("11:50");

        updatedCourse.setStartCourseTime(startCourseTime);
        updatedCourse.setEndCourseTime(endCourseTime);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(existingCourse);

        CourseService spyCourseService = spy(courseService);
        doReturn(false).when(spyCourseService).isTimeAvailableForCourse(any(Course.class));

        ScheduleTimeException exception = assertThrows(ScheduleTimeException.class, () -> spyCourseService.setScheduleTimeForCourse(updatedCourse));

        assertEquals("This time is not available.Set another time range or day of the week!", exception.getMessage());

        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository, never()).save(updatedCourse);

    }

    @Test
    void setScheduleTimeForCourse_shouldUpdateCourse_whenCourseTimeIsAvailable() {

        long courseId = 1;

        Course existingCourse = new Course();
        existingCourse.setCourseId(courseId);
        existingCourse.setCourseName("Test course name");
        existingCourse.setCourseDescription("Test course description");

        Course updatedCourse = new Course();
        updatedCourse.setCourseId(courseId);
        updatedCourse.setCourseName("Test course name");
        updatedCourse.setCourseDescription("Test course description");
        updatedCourse.setDayOfWeek(DayOfWeek.MONDAY.getValue());

        LocalTime startCourseTime = LocalTime.parse("18:30");
        LocalTime endCourseTime = LocalTime.parse("20:00");

        updatedCourse.setStartCourseTime(startCourseTime);
        updatedCourse.setEndCourseTime(endCourseTime);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(existingCourse);

        CourseService spyCourseService = spy(courseService);
        doReturn(true).when(spyCourseService).isTimeAvailableForCourse(any(Course.class));

        spyCourseService.setScheduleTimeForCourse(updatedCourse);

        verify(courseRepository).findCourseByCourseId(courseId);
        verify(courseRepository).save(updatedCourse);

    }

}
