package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.GroupCourse;
import com.example.universitycms.repository.CourseRepository;
import com.example.universitycms.repository.GroupCourseRepository;
import com.example.universitycms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupCourseService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupCourseRepository groupCourseRepository;

    public void assignCourseOnGroup(long groupId, long courseId) {

        if(!groupRepository.existsByGroupId(groupId)) {
            throw new IllegalArgumentException("Group with this Id doesn't exist!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        if(groupCourseRepository.existsByGroupAndCourse(group, course)) {
            throw new IllegalArgumentException("This group is already assigned on this course!");
        }

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.save(groupCourse);
    }

    public void removeCourseFromGroup(long groupId, long courseId) {

        if(!groupRepository.existsByGroupId(groupId)) {
            throw new IllegalArgumentException("Group with this Id doesn't exist!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with this Id doesn't exist!");
        }

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        if(!groupCourseRepository.existsByGroupAndCourse(group, course)) {
            throw new IllegalArgumentException("This group is not assigned on this course!");
        }

        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(group, course);

        groupCourseRepository.delete(groupCourse);
    }

}
