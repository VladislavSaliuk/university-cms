package com.example.universitycms.service;


import com.example.universitycms.exception.CourseNotFoundException;
import com.example.universitycms.exception.GroupAlreadyAssignedException;
import com.example.universitycms.exception.GroupNotAssignedException;
import com.example.universitycms.exception.GroupNotFoundException;
import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.GroupCourse;
import com.example.universitycms.repository.CourseRepository;
import com.example.universitycms.repository.GroupCourseRepository;
import com.example.universitycms.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupCourseServiceTest {

    @Autowired
    GroupCourseService groupCourseService;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    GroupCourseRepository groupCourseRepository;


    @Test
    void assignCourseOnGroup_shouldInsertGroupCourseToDataBase_whenInputContainsExistingData() {

        long groupId = 1;
        long courseId = 1;

        Group group = new Group();

        Course course = new Course();

        GroupCourse groupCourse = new GroupCourse(group, course);

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId)).thenReturn(false);

        groupCourseService.assignCourseOnGroup(groupId, courseId);

        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository).save(groupCourse);
    }


    @Test
    void assignCourseOnGroup_shouldThrowException_whenInputContainsNotExistingGroupId() {

        long groupId = 100;
        long courseId = 1;

        Course course = new Course();

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(null);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("Group with this Id doesn't exist!",exception.getMessage());

        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).save(any(GroupCourse.class));
    }

    @Test
    void assignCourseOnGroup_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long groupId = 1;
        long courseId = 100;

        Group group = new Group();

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());

        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).save(any(GroupCourse.class));
    }

    @Test
    void assignCourseOnGroup_shouldThrowException_whenCourseIsAlreadyAssigned() {
        long groupId = 4;
        long courseId = 4;

        Group group = new Group();

        Course course = new Course();

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId))
                .thenReturn(true)
                .thenThrow(IllegalArgumentException.class);

        GroupAlreadyAssignedException exception = assertThrows(GroupAlreadyAssignedException.class, () ->
                groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("This group is already assigned on this course!", exception.getMessage());


        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).save(any(GroupCourse.class));
    }


    @Test
    void removeCourseFromGroup_shouldDeleteGroupCourse_whenInputContainsExistingData() {

        long groupId = 1;
        long courseId = 1;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        GroupCourse groupCourse = new GroupCourse(group, course);

        when(groupRepository.existsByGroupId(groupId)).thenReturn(true);

        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);

        when(groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId)).thenReturn(true);

        when(groupCourseRepository.findByGroup_GroupIdAndCourse_CourseId(groupId, courseId))
                .thenReturn(groupCourse);

        groupCourseService.removeCourseFromGroup(groupId, courseId);

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupCourseRepository).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository).findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository).delete(groupCourse);
    }


    @Test
    void removeCourseFromGroup_shouldThrowException_whenInputContainsNotExistingGroupId() {
        long groupId = 100;
        long courseId = 1;

        when(groupRepository.existsByGroupId(groupId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupCourseService.removeCourseFromGroup(groupId, courseId));

        assertEquals("Group with this Id doesn't exist!", exception.getMessage());

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository, never()).existsByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).delete(any(GroupCourse.class));
    }


    @Test
    void removeCourseFromGroup_shouldThrowException_whenInputContainsNotExistingCourseId() {

        long groupId = 1;
        long courseId = 100;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(true);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> groupCourseService.removeCourseFromGroup(groupId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).delete(any(GroupCourse.class));
    }

    @Test
    void removeCourseFromGroup_shouldThrowException_whenCourseIsNotAssigned() {
        long groupId = 1;
        long courseId = 2;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(true);

        when(courseRepository.existsByCourseId(courseId))
                .thenReturn(true);

        when(groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId))
                .thenReturn(false);

        GroupNotAssignedException exception = assertThrows(GroupNotAssignedException.class, () ->
                groupCourseService.removeCourseFromGroup(groupId, courseId));

        assertEquals("This group is not assigned on this course!", exception.getMessage());

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupCourseRepository).existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        verify(groupCourseRepository, never()).delete(any(GroupCourse.class));
    }


    @Test
    void getUnassignedCoursesForGroup_shouldReturnCorrectCourseList_whenInputContainsExistingGroupId() {

        long groupId = 3;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        Group group = new Group();

        GroupCourse groupCourse = new GroupCourse(group, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(groupCourseRepository.findByGroup_GroupId(groupId)).thenReturn(Collections.singletonList(groupCourse));

        List<Course> unassignedCourses = groupCourseService.getUnassignedCoursesForGroup(groupId);

        assertEquals(9, unassignedCourses.size());
        assertEquals(3, unassignedCourses.get(2).getCourseId());

        verify(courseRepository).findAll();
        verify(groupCourseRepository).findByGroup_GroupId(groupId);
    }

    @Test
    void getUnassignedCoursesForGroup_shouldReturnAllCourses_whenInputContainsNotExistingGroupId() {

        long groupId = 100;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        Group group = new Group();

        GroupCourse groupCourse = new GroupCourse(group, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(groupCourseRepository.findByGroup_GroupId(groupId)).thenReturn(Collections.singletonList(groupCourse));

        List<Course> unassignedCourses = groupCourseService.getUnassignedCoursesForGroup(100);

        assertEquals(9, unassignedCourses.size());

        verify(courseRepository).findAll();
        verify(groupCourseRepository).findByGroup_GroupId(groupId);
    }

    @Test
    void getAssignedCoursesForGroup_shouldReturnEmptyCourseList_whenInputContainsExistingGroupId() {

        long groupId = 3;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        Group group = new Group();

        GroupCourse groupCourse = new GroupCourse(group, courseList.get(2));

        when(groupCourseRepository.findByGroup_GroupId(groupId)).thenReturn(Collections.singletonList(groupCourse));

        List<Course> assignedCourses = groupCourseService.getAssignedCoursesForGroup(groupId);

        assertEquals(1, assignedCourses.size());

        verify(groupCourseRepository).findByGroup_GroupId(groupId);
    }

    @Test
    void getAssignedCoursesForGroup_shouldReturnCorrectCourseList_whenInputContainsNotExistingGroupId() {

        long groupId = 100;

        List<Course> courseList = LongStream.range(0, 10)
                .mapToObj(courseId -> {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    return course;
                })
                .collect(Collectors.toList());

        Group group = new Group();

        GroupCourse groupCourse = new GroupCourse(group, courseList.get(2));

        when(courseRepository.findAll()).thenReturn(courseList);
        when(groupCourseRepository.findByGroup_GroupId(100)).thenReturn(Collections.singletonList(groupCourse));

        List<Course> unassignedCourses = groupCourseService.getAssignedCoursesForGroup(groupId);

        assertEquals(1, unassignedCourses.size());

        verify(groupCourseRepository).findByGroup_GroupId(groupId);
    }


}
