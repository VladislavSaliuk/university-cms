package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.RoleId;
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
    public void assignTeacherOnCourse(long userId, long courseId) {

        User user = userRepository.findUserByUserId(userId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        if(user == null) {
            throw new IllegalArgumentException("Teacher with this Id doesn't exist!");
        }

        if(user.getRole().getRoleId() != RoleId.TEACHER.getValue()) {
            throw new IllegalArgumentException("This user is not a teacher!");
        }

        if(course == null) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        if(userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)) {
            throw new IllegalArgumentException("This teacher is already assigned on this course!");
        }

        UserCourse userCourse = new UserCourse(user, course);
        userCourseRepository.save(userCourse);
    }

    public void removeTeacherFromCourse(long userId, long courseId) {

        User user = userRepository.findUserByUserId(userId);

        if(user == null) {
            throw new IllegalArgumentException("Teacher with this Id doesn't exist!");
        }

        if(user.getRole().getRoleId() != RoleId.TEACHER.getValue()) {
            throw new IllegalArgumentException("This user is not a teacher!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        if(!userCourseRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)) {
            throw new IllegalArgumentException("This teacher is not assigned on this course!");
        }

        UserCourse userCourse = userCourseRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        userCourseRepository.delete(userCourse);
    }
    public List<Course> getUnassignedCoursesForTeacher(long userId) {

        User user = userRepository.findUserByUserId(userId);

        if (user == null) {
            throw new IllegalArgumentException("Teacher with this Id doesn't exist!");
        }

        if (user.getRole().getRoleId() != RoleId.TEACHER.getValue()) {
            throw new IllegalArgumentException("This user is not a teacher!");
        }

        List<Course> courseList = courseRepository.findAll();

        List<Course> assignedCourses = userCourseRepository.findByUser_UserId(userId)
                .stream()
                .map(UserCourse::getCourse)
                .collect(Collectors.toList());

        List<Course> unassignedCourses = courseList.stream()
                .filter(course -> !assignedCourses.contains(course))
                .collect(Collectors.toList());

        return unassignedCourses;
    }


    public List<Course> getAssignedCoursesForTeacher(long userId) {

        User user = userRepository.findUserByUserId(userId);

        if(user == null) {
            throw new IllegalArgumentException("This user is not a teacher!");
        }

        if (user.getRole().getRoleId() != RoleId.TEACHER.getValue()) {
            throw new IllegalArgumentException("This user is not a teacher!");
        }

        return userCourseRepository.findByUser_UserId(userId)
                .stream()
                .map(UserCourse::getCourse)
                .collect(Collectors.toList());
    }

}