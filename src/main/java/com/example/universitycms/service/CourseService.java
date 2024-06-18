package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseService {

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

        if (course.getCourseName() == null) {
            throw new IllegalArgumentException("Course must contains name!");
        }

        if (!existingCourse.getCourseName().equals(course.getCourseName()) && courseRepository.existsByCourseName(course.getCourseName())) {
            throw new IllegalArgumentException("Course with this name already exists!");
        }

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setCourseDescription(course.getCourseDescription());

        courseRepository.save(existingCourse);
    }


    public Course getCourseByCourseId(long subjectId) {

        if (!courseRepository.existsByCourseId(subjectId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exists!");
        }

        return courseRepository.findCourseByCourseId(subjectId);
    }

}
