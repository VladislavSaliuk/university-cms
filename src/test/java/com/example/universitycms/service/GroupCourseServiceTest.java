package com.example.universitycms.service;


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
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        GroupCourse groupCourse = new GroupCourse(group, course);

        when(groupRepository.existsByGroupId(groupId)).thenReturn(true);
        when(courseRepository.existsByCourseId(courseId)).thenReturn(true);

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(groupCourseRepository.existsByGroupAndCourse(group, course)).thenReturn(false);

        groupCourseService.assignCourseOnGroup(groupId, courseId);

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository).save(groupCourse);
    }


    @Test
    void assignCourseOnGroup_shouldThrowException_whenInputContainsNotExistingGroupId() {

        long groupId = 100;
        long courseId = 1;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("Group with this Id doesn't exist!",exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository, never()).existsByCourseId(courseId);
        verify(groupRepository, never()).findGroupByGroupId(groupId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).save(any(GroupCourse.class));
    }

    @Test
    void assignCourseOnGroup_shouldThrowException_whenInputContainsNotExistingCourseId() {

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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository, never()).findGroupByGroupId(groupId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).save(any(GroupCourse.class));
    }

    @Test
    void assignCourseOnGroup_shouldThrowException_whenCourseIsAlreadyAssigned() {
        long groupId = 4;
        long courseId = 4;

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

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(groupCourseRepository.existsByGroupAndCourse(group, course))
                .thenReturn(true)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("This group is already assigned on this course!", exception.getMessage());

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroupAndCourse(group, course);
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

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);
        when(courseRepository.findCourseByCourseId(courseId)).thenReturn(course);

        when(groupCourseRepository.existsByGroupAndCourse(group, course)).thenReturn(true);

        when(groupCourseRepository.findByGroupAndCourse(group, course))
                .thenReturn(groupCourse);

        groupCourseService.removeCourseFromGroup(groupId, courseId);

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository).findByGroupAndCourse(group, course);
        verify(groupCourseRepository).delete(groupCourse);
    }


    @Test
    void removeCourseFromGroup_shouldThrowException_whenInputContainsNotExistingGroupId() {

        long groupId = 100;
        long courseId = 1;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test course name");
        course.setCourseDescription("Test description");

        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupCourseService.assignCourseOnGroup(groupId, courseId));

        assertEquals("Group with this Id doesn't exist!",exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository, never()).existsByCourseId(courseId);
        verify(groupRepository, never()).findGroupByGroupId(groupId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).findByGroupAndCourse(group, course);
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupCourseService.removeCourseFromGroup(groupId, courseId));

        assertEquals("Course with this Id doesn't exist!",exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository, never()).findGroupByGroupId(groupId);
        verify(courseRepository, never()).findCourseByCourseId(courseId);
        verify(groupCourseRepository, never()).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).findByGroupAndCourse(group, course);
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

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(courseRepository.findCourseByCourseId(courseId))
                .thenReturn(course);

        when(groupCourseRepository.existsByGroupAndCourse(group, course))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                groupCourseService.removeCourseFromGroup(groupId, courseId));

        assertEquals("This group is not assigned on this course!", exception.getMessage());

        verify(groupRepository).existsByGroupId(groupId);
        verify(courseRepository).existsByCourseId(courseId);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(courseRepository).findCourseByCourseId(courseId);
        verify(groupCourseRepository).existsByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).findByGroupAndCourse(group, course);
        verify(groupCourseRepository, never()).delete(any(GroupCourse.class));
    }


}
