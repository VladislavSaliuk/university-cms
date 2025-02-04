package com.example.myschedule.service;

import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.entity.Course;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.*;
import com.example.myschedule.repository.CourseRepository;
import com.example.myschedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    public void createCourse(CourseDTO courseDTO) {

        log.info("Attempting to create a new course with name: {}", courseDTO.getCourseName());

        if (courseRepository.existsByCourseName(courseDTO.getCourseName())) {
            log.warn("Course creation failed. Course with name '{}' already exists!", courseDTO.getCourseName());
            throw new CourseException("Course with " + courseDTO.getCourseName() + " name already exists!");
        }

        User user = userRepository.findById(courseDTO.getTeacherDTO().getUserId())
                .orElseThrow(() -> {
                    log.error("Teacher with ID '{}' not found!", courseDTO.getTeacherDTO().getUserId());
                    return new UserNotFoundException("Teacher with " + courseDTO.getTeacherDTO().getUserId() + " Id not found!");
                });

        if (!user.getRole().name().equals(Role.TEACHER.name())) {
            log.error("User with ID '{}' is not a teacher!", courseDTO.getTeacherDTO().getUserId());
            throw new UserException("User with " + courseDTO.getTeacherDTO().getUserId() + " Id is not a teacher!");
        }

        Course course = Course.builder()
                .courseName(courseDTO.getCourseName())
                .courseDescription(courseDTO.getCourseDescription())
                .user(user)
                .build();

        courseRepository.save(course);

        log.info("Course with name '{}' successfully created with ID: {}", courseDTO.getCourseName(), course.getCourseId());
    }

    @Transactional
    public void updateCourse(CourseDTO courseDTO) {
        log.info("Attempting to update course with ID: {}", courseDTO.getCourseId());

        Course updatedCourse = courseRepository.findById(courseDTO.getCourseId())
                .orElseThrow(() -> {
                    log.error("Course update failed. Course with ID '{}' not found!", courseDTO.getCourseId());
                    return new CourseNotFoundException("Course with " + courseDTO.getCourseId() + " Id not found!");
                });

        if (!updatedCourse.getCourseName().equals(courseDTO.getCourseName())) {
            if (courseRepository.existsByCourseName(courseDTO.getCourseName())) {
                log.warn("Course update failed. Course with name '{}' already exists!", courseDTO.getCourseName());
                throw new CourseException("Course with " + courseDTO.getCourseName() + " name already exists!");
            }
        }

        User user = userRepository.findById(courseDTO.getTeacherDTO().getUserId())
                .orElseThrow(() -> {
                    log.error("Teacher with ID '{}' not found!", courseDTO.getTeacherDTO().getUserId());
                    return new UserNotFoundException("Teacher with " + courseDTO.getTeacherDTO().getUserId() + " Id not found!");
                });

        if (!user.getRole().name().equals(Role.TEACHER.name())) {
            log.error("User with ID '{}' is not a teacher!", courseDTO.getTeacherDTO().getUserId());
            throw new UserException("User with " + courseDTO.getTeacherDTO().getUserId() + " Id is not a teacher!");
        }

        updatedCourse.setCourseName(courseDTO.getCourseName());
        updatedCourse.setCourseDescription(courseDTO.getCourseDescription());
        updatedCourse.setUser(user);

        log.info("Course with ID '{}' successfully updated.", courseDTO.getCourseId());
    }

    public List<CourseDTO> getAll() {
        log.info("Fetching all courses...");
        List<CourseDTO> courses = courseRepository.findAll()
                .stream()
                .map(CourseDTO::toCourseDTO)
                .collect(Collectors.toList());

        Collections.reverse(courses);

        log.info("Found {} courses.", courses.size());
        return courses;
    }

    public CourseDTO getById(long courseId) {
        log.info("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId)
                .map(course -> {
                    log.info("Course with ID '{}' found.", courseId);
                    return CourseDTO.toCourseDTO(course);
                })
                .orElseThrow(() -> {
                    log.error("Course with ID '{}' not found!", courseId);
                    return new CourseNotFoundException("Course with " + courseId + " Id not found!");
                });
    }

    public CourseDTO getByCourseName(String courseName) {
        log.info("Fetching course with name: {}", courseName);
        return courseRepository.findByCourseName(courseName)
                .map(course -> {
                    log.info("Course with name '{}' found.", courseName);
                    return CourseDTO.toCourseDTO(course);
                })
                .orElseThrow(() -> {
                    log.error("Course with name '{}' not found!", courseName);
                    return new CourseNotFoundException("Course with " + courseName + " name not found!");
                });
    }

    public void removeById(long courseId) {
        log.info("Attempting to delete course with ID: {}", courseId);
        if (!courseRepository.existsById(courseId)) {
            log.error("Course deletion failed. Course with ID '{}' not found!", courseId);
            throw new CourseNotFoundException("Course with " + courseId + " Id not found!");
        }

        courseRepository.deleteById(courseId);
        log.info("Course with ID '{}' successfully deleted.", courseId);
    }

}
