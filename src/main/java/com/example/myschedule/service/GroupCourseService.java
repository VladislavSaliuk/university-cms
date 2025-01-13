package com.example.myschedule.service;

import com.example.myschedule.exception.CourseNotFoundException;
import com.example.myschedule.exception.GroupAlreadyAssignedException;
import com.example.myschedule.exception.GroupNotAssignedException;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.model.Course;
import com.example.myschedule.model.Group;
import com.example.myschedule.model.GroupCourse;
import com.example.myschedule.repository.CourseRepository;
import com.example.myschedule.repository.GroupCourseRepository;
import com.example.myschedule.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupCourseService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupCourseRepository groupCourseRepository;

    public void assignCourseOnGroup(long groupId, long courseId) {

        Group group = groupRepository.findGroupByGroupId(groupId);

        Course course = courseRepository.findCourseByCourseId(courseId);

        if(group == null) {
            throw new GroupNotFoundException("Group with this Id doesn't exist!");
        }

        if(course == null) {
            throw new CourseNotFoundException("Course with this Id doesn't exist!");
        }

        if(groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId)) {
            throw new GroupAlreadyAssignedException("This group is already assigned on this course!");
        }

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.save(groupCourse);
    }

    public void removeCourseFromGroup(long groupId, long courseId) {

        if(!groupRepository.existsByGroupId(groupId)) {
            throw new GroupNotFoundException("Group with this Id doesn't exist!");
        }

        if(!courseRepository.existsByCourseId(courseId)) {
            throw new CourseNotFoundException("Course with this Id doesn't exist!");
        }

        if(!groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId)) {
            throw new GroupNotAssignedException("This group is not assigned on this course!");
        }

        GroupCourse groupCourse = groupCourseRepository.findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);

        groupCourseRepository.delete(groupCourse);
    }


    public List<Course> getUnassignedCoursesForGroup(long groupId) {
        List<Course> courseList = courseRepository.findAll();

        List<Course> assignedCourses = groupCourseRepository.findByGroup_GroupId(groupId)
                .stream()
                .map(GroupCourse::getCourse)
                .collect(Collectors.toList());

        List<Course> unassignedCourses = courseList.stream()
                .filter(course -> !assignedCourses.contains(course))
                .collect(Collectors.toList());

        return unassignedCourses;
    }

    public List<Course> getAssignedCoursesForGroup(long groupId) {
        return groupCourseRepository.findByGroup_GroupId(groupId)
                .stream()
                .map(GroupCourse::getCourse)
                .collect(Collectors.toList());
    }

}
