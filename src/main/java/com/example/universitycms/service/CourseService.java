package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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

    public Course getCourseByCourseId(long courseId) {

        Course course = courseRepository.findCourseByCourseId(courseId);

        if(course == null) {
            throw new IllegalArgumentException("Course with this Id doesn't exists!");
        }

        return course;
    }

    public void setScheduleTimeForCourse(Course course) {

        Course existingCourse = courseRepository.findCourseByCourseId(course.getCourseId());

        if(existingCourse == null) {
            throw new IllegalArgumentException("This course doesn't exist!");
        }

        if(course.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Day of week is not set!");
        }

        existingCourse.setDayOfWeek(course.getDayOfWeek());

        if(isTimeAvailableForCourse(existingCourse)){

            LocalTime startCourseTime = course.getStartCourseTime();
            LocalTime endCourseTime = course.getEndCourseTime();

            existingCourse.setStartCourseTime(startCourseTime);
            existingCourse.setEndCourseTime(endCourseTime);

            courseRepository.save(existingCourse);
        } else {
            throw new IllegalArgumentException("This time is not available.Set another time range or day of the week!");
        }

    }

    protected boolean isTimeAvailableForCourse(Course course) {

        if (course.getStartCourseTime().isAfter(course.getEndCourseTime())) {
            return false;
        }

        List<Course> allCoursesByDayOfWeek = courseRepository.findAllByDayOfWeek(course.getDayOfWeek())
                .stream()
                .sorted((c1, c2) -> c1.getStartCourseTime().compareTo(c2.getStartCourseTime()))
                .collect(Collectors.toList());

        if (allCoursesByDayOfWeek.isEmpty()) {
            return true;
        }

        LocalTime currentCourseStartTime = course.getStartCourseTime();
        LocalTime currentCourseEndTime = course.getEndCourseTime();

        for (Course existingCourse : allCoursesByDayOfWeek) {

            LocalTime existingCourseStartTime = existingCourse.getStartCourseTime();
            LocalTime existingCourseEndTime = existingCourse.getEndCourseTime();

            if (currentCourseStartTime.isBefore(existingCourseEndTime) && currentCourseEndTime.isAfter(existingCourseStartTime)) {
                return false;
            }
        }

        return true;
    }

}
