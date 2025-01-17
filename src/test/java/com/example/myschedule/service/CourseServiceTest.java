package com.example.myschedule.service;

import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.dto.TeacherDTO;
import com.example.myschedule.entity.Course;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.CourseException;
import com.example.myschedule.exception.CourseNotFoundException;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.CourseRepository;
import com.example.myschedule.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @InjectMocks
    CourseService courseService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    UserRepository userRepository;
    Course course;
    CourseDTO courseDTO;
    User user;
    TeacherDTO teacherDTO;
    @BeforeEach
    void setUp(){

        long userId = 1L;
        String username = "username";
        String password = "$2y$10$fGkGf4h1aCSlfPBpwTfvGerNI4puZTLVQeZLLCpSrqh8WcPuMtr7a";
        String email = "email@gmail.com";
        String firstname = "Firstname";
        String lastname = "Lastname";

        user = User.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.TEACHER)
                .status(Status.ACTIVE)
                .build();

        teacherDTO = TeacherDTO.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .status(Status.ACTIVE)
                .build();

        String courseName = "Group name";
        String courseDescription = "Course description";

        course = Course.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .user(user)
                .build();

        courseDTO = CourseDTO.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .teacherDTO(teacherDTO)
                .build();

    }

    @Test
    void createCourse_shouldReturnCourseDTO() {

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.of(user));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);

        CourseDTO actualCourseDTO = courseService.createCourse(courseDTO);

        assertNotNull(actualCourseDTO);
        assertEquals(courseDTO, actualCourseDTO);

        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());
        verify(courseRepository).save(any(Course.class));

    }


    @Test
    void createCourse_shouldThrowException_whenCourseNameRepeats() {

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(true);

        CourseException exception = assertThrows(CourseException.class, () -> courseService.createCourse(courseDTO));

        assertEquals("Course with " + courseDTO.getCourseName() + " name already exists!", exception.getMessage());

        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository, never()).findById(courseDTO.getTeacherDTO().getUserId());
        verify(courseRepository, never()).save(any(Course.class));

    }

    @Test
    void createCourse_shouldThrowException_whenUserNotFound() {

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> courseService.createCourse(courseDTO));

        assertEquals("Teacher with " + courseDTO.getTeacherDTO().getUserId() + " Id not found!", exception.getMessage());

        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());
        verify(courseRepository, never()).save(any(Course.class));

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STUDENT", "ADMIN", "STUFF"})
    void createCourse_shouldThrowException_whenUserIsNotTeacher(Role role) {

        user.setRole(role);

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> courseService.createCourse(courseDTO));

        assertEquals("User with " + courseDTO.getTeacherDTO().getUserId() + " Id is not a teacher!", exception.getMessage());

        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());
        verify(courseRepository, never()).save(any(Course.class));

    }

    @Test
    void updateCourse_shouldReturnCourseDTO() {

        courseDTO.setCourseName("Course name 2");

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.of(user));

        CourseDTO actualCourseDTO = courseService.updateCourse(courseDTO);

        assertNotNull(actualCourseDTO);
        assertEquals(courseDTO, actualCourseDTO);

        verify(courseRepository).findById(courseDTO.getCourseId());
        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());

    }

    @Test
    void updateCourse_shouldThrowException_whenCourseNotFound() {

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(courseDTO));

        assertEquals("Course with " + courseDTO.getCourseId() + " Id not found!", exception.getMessage());

        verify(courseRepository).findById(courseDTO.getCourseId());
        verify(courseRepository, never()).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository, never()).findById(courseDTO.getTeacherDTO().getUserId());

    }
    @Test
    void updateCourse_shouldThrowException_whenCourseNameRepeats() {

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(true);

        CourseException exception = assertThrows(CourseException.class, () -> courseService.updateCourse(courseDTO));

        assertEquals("Course with " + courseDTO.getCourseName() + " name already exists!", exception.getMessage());

        verify(courseRepository).findById(courseDTO.getCourseId());
        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository, never()).findById(courseDTO.getTeacherDTO().getUserId());

    }


    @Test
    void updateCourse_shouldThrowException_whenUserNotFound() {

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> courseService.updateCourse(courseDTO));

        assertEquals("Teacher with " + courseDTO.getTeacherDTO().getUserId() + " Id not found!", exception.getMessage());

        verify(courseRepository).findById(courseDTO.getCourseId());
        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STUDENT", "ADMIN", "STUFF"})
    void updateCourse_shouldThrowException_whenUserIsNotTeacher(Role role) {

        user.setRole(role);

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseName(courseDTO.getCourseName()))
                .thenReturn(false);

        when(userRepository.findById(courseDTO.getTeacherDTO().getUserId()))
                .thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> courseService.updateCourse(courseDTO));

        assertEquals("User with " + courseDTO.getTeacherDTO().getUserId() + " Id is not a teacher!", exception.getMessage());

        verify(courseRepository).findById(courseDTO.getCourseId());
        verify(courseRepository).existsByCourseName(courseDTO.getCourseName());
        verify(userRepository).findById(courseDTO.getTeacherDTO().getUserId());

    }

    @Test
    void getAll_shouldReturnCourseDTOList() {

        when(courseRepository.findAll())
                .thenReturn(List.of(course));

        List<CourseDTO> courseDTOList = courseService.getAll();

        assertNotNull(courseDTOList);
        assertFalse(courseDTOList.isEmpty());
        assertEquals(1, courseDTOList.size());

        verify(courseRepository).findAll();

    }

    @Test
    void getById_shouldReturnCourseDTO() {

        long courseId = 1L;

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        CourseDTO courseDTO = courseService.getById(courseId);

        assertNotNull(courseDTO);
        assertEquals(this.courseDTO, courseDTO);


        verify(courseRepository).findById(courseId);

    }

    @Test
    void getById_shouldThrowException_whenCourseNotFound() {

        long courseId = 100L;

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getById(courseId));

        assertEquals("Course with " + courseId + " Id not found!", exception.getMessage());

        verify(courseRepository).findById(courseId);

    }

    @Test
    void getByCourseName_shouldReturnCourseDTO() {

        String courseName = "Introduction to Programming";

        when(courseRepository.findByCourseName(courseName))
                .thenReturn(Optional.of(course));

        CourseDTO actualCourseDTO = courseService.getByCourseName(courseName);

        assertNotNull(actualCourseDTO);
        assertEquals(courseDTO, actualCourseDTO);

        verify(courseRepository).findByCourseName(courseName);

    }

    @Test
    void getByCourseName_shouldThrowException_whenCourseNotFound() {

        String courseName = "Course name 1";

        when(courseRepository.findByCourseName(courseName))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getByCourseName(courseName));

        assertEquals("Course with " + courseName + " name not found!", exception.getMessage());

        verify(courseRepository).findByCourseName(courseName);

    }

    @Test
    void removeById_shouldDeleteGroup() {

        long groupId = 1L;

        when(courseRepository.existsById(groupId))
                .thenReturn(true);

        doNothing().when(courseRepository).deleteById(groupId);

        courseService.removeById(groupId);

        verify(courseRepository).existsById(groupId);
        verify(courseRepository).deleteById(groupId);

    }

    @Test
    void removeById_shouldThrowException_whenCourseNotFound() {

        long courseId = 100L;

        when(courseRepository.existsById(courseId))
                .thenReturn(false);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.removeById(courseId));

        assertEquals("Course with " + courseId + " Id not found!", exception.getMessage());

        verify(courseRepository).existsById(courseId);
        verify(courseRepository, never()).deleteById(courseId);

    }

}
