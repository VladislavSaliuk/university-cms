package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.User;
import com.example.universitycms.model.UserCourse;
import com.example.universitycms.repository.CourseRepository;
import com.example.universitycms.repository.UserCourseRepository;
import com.example.universitycms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;
    public void assignUserOnCourse(long userId, long courseId) {

        if(!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User with this Id doesn't exist!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        if(userCourseRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalArgumentException("This user is already assigned on this course!");
        }

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);
    }

    public void removeUserFromCourse(long userId, long courseId) {

        if(!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User with this Id doesn't exist!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        if(!userCourseRepository.existsByUserAndCourse(user, course)) {
            throw new IllegalArgumentException("This user is not assigned on this course!");
        }

        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course);
        userCourseRepository.delete(userCourse);
    }
    public List<Course> getUnassignedCoursesForUser(long userId) {

        List<Course> courseList = courseRepository.findAll();

        List<Course> assignedCourses = userCourseRepository
                .findAll()
                .stream()
                .filter(userCourse -> userCourse.getUser().getUserId() == userId)
                .map(UserCourse::getCourse)
                .collect(Collectors.toList());

        List<Course> unassignedCourses = courseList.stream()
                .filter(course -> !assignedCourses.contains(course))
                .collect(Collectors.toList());

        return unassignedCourses;
    }

    public List<Course> getAssignedCoursesForUser(long userId) {
        return userCourseRepository.findAll()
                .stream()
                .filter(userCourse -> userCourse.getUser().getUserId() == userId)
                .map(UserCourse::getCourse)
                .collect(Collectors.toList());
    }


}