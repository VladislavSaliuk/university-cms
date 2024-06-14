package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    public void createCourse(Course course) {

        if (course.getCourseName() == null) {
            throw new IllegalArgumentException("Course must contains name!");
        }

        if (courseRepository.existsByCourseName(course.getCourseName())) {
            throw new IllegalArgumentException("Course with this name already exists!");
        }

        courseRepository.save(course);

    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getCourseByCourseName(String courseName) {

        if (courseName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if (!courseRepository.existsByCourseName(courseName)) {
            throw new IllegalArgumentException("Course name doesn't exists!");
        }

        return courseRepository.findCourseByCourseName(courseName);
    }

    public void clearAll() {
        courseRepository.deleteAll();
    }

    public void removeCourseByCourseName(String courseName) {

        if (courseName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if (!courseRepository.existsByCourseName(courseName)) {
            throw new IllegalArgumentException("Course with this name doesn't exist!");
        }

        courseRepository.deleteCourseByCourseName(courseName);
    }

    public void removeCourseByCourseId(long courseId) {

        if (!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this id doesn't exist!");
        }

        courseRepository.deleteCourseByCourseId(courseId);

    }

    public void updateCourse(Course course) {

        Course existingCourse = courseRepository.findCourseByCourseId(course.getCourseId());

        if (existingCourse == null) {
            throw new IllegalArgumentException("This course doesn't exist!");
        }

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setCourseDescription(course.getCourseDescription());

        if (existingCourse.getCourseName() == null) {
            throw new IllegalArgumentException("Course must contains name!");
        }

        if (courseRepository.existsByCourseName(existingCourse.getCourseName())) {
            throw new IllegalArgumentException("Course with this name already exists!");
        }

        courseRepository.save(existingCourse);

    }


    public Course getCourseByCourseId(long subjectId) {

        if (!courseRepository.existsByCourseId(subjectId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exists!");
        }

        return courseRepository.findCourseByCourseId(subjectId);
    }

}
