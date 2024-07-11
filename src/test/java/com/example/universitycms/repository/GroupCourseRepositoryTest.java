package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.GroupCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupCourseRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    GroupCourseRepository groupCourseRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void save_shouldInsertGroupCourseToDatabase_whenGroupCourseDoesNotExist() {
        long groupId = 1;
        long courseId = 3;

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.save(groupCourse);

        assertEquals(11, groupCourseRepository.count());
        assertEquals(10, groupRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void save_shouldNotInsertGroupCourseToDatabase_whenGroupCourseExists() {
        long groupId = 1;
        long courseId = 1;

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.save(groupCourse);

        assertEquals(10, groupCourseRepository.count());
        assertEquals(10, groupRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void delete_shouldDeleteGroupCourse_whenInputContainsExistingGroupCourse() {
        long groupId = 1;
        long courseId = 1;

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.delete(groupCourse);

        assertEquals(9, groupCourseRepository.count());
        assertEquals(10, groupRepository.count());
        assertEquals(10, courseRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void delete_shouldNotDeleteGroupCourse_whenInputContainsNotExistingGroupCourse() {
        long groupId = 1;
        long courseId = 10;

        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);

        GroupCourse groupCourse = new GroupCourse(group, course);
        groupCourseRepository.delete(groupCourse);

        assertEquals(10, groupCourseRepository.count());
        assertEquals(10, groupRepository.count());
        assertEquals(10, courseRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroup_GroupIdAndCourse_CourseId_shouldReturnGroupCourse_whenInputContainsExistingData() {
        long groupId = 1;
        long courseId = 1;
        GroupCourse groupCourse = groupCourseRepository.findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertNotNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroup_GroupIdAndCourse_CourseId_shouldReturnNull_whenInputContainsNotExistingGroupId() {
        long groupId = 100;
        long courseId = 1;
        GroupCourse groupCourse = groupCourseRepository.findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroup_GroupIdAndCourse_CourseId_shouldReturnNull_whenInputContainsNotExistingCourseId() {
        long groupId = 1;
        long courseId = 100;
        GroupCourse groupCourse = groupCourseRepository.findByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertNull(groupCourse);
    }


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroup_GroupIdAndCourse_CourseId_shouldReturnTrue_whenInputContainsExistingData() {
        long groupId = 1;
        long courseId = 1;
        boolean isGroupCourseExists = groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertTrue(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroup_GroupIdAndCourse_CourseId_shouldReturnFalse_whenInputContainsNotExistingGroupId() {
        long groupId = 100;
        long courseId = 1;
        boolean isGroupCourseExists = groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertFalse(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroup_GroupIdAndCourse_CourseId_shouldReturnFalse_whenInputContainsNotExistingCourseId() {
        long groupId = 1;
        long courseId = 100;
        boolean isGroupCourseExists = groupCourseRepository.existsByGroup_GroupIdAndCourse_CourseId(groupId, courseId);
        assertFalse(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroup_GroupId_shouldReturnGroupCourseList_whenInputContainsExistingGroupId() {
        long groupId = 3;
        List<GroupCourse> actualGroupCourseList = groupCourseRepository.findByGroup_GroupId(groupId);
        assertNotNull(actualGroupCourseList);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroup_GroupId_shouldReturnEmptyList_whenInputContainsNotExistingGroupId() {
        long groupId = 100;
        List<GroupCourse> actualGroupCourseList = groupCourseRepository.findByGroup_GroupId(groupId);
        assertEquals(Collections.emptyList(), actualGroupCourseList);
    }
}
