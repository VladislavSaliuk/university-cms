package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.GroupCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

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
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroupAndCourse_shouldReturnGroupCourse_whenInputContainsExistingData() {
        long groupId = 1;
        long courseId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(group, course);
        assertNotNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroupAndCourse_shouldReturnNull_whenInputContainsNotExistingGroup() {
        long groupId = 100;
        long courseId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(group, course);
        assertNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroupAndCourse_shouldReturnNull_whenInputContainsNotExistingCourse() {
        long groupId = 1;
        long courseId = 100;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(group, course);
        assertNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroupAndCourse_shouldReturnNull_whenInputDoesNotContainGroup() {
        long courseId = 1;
        Course course = courseRepository.findCourseByCourseId(courseId);
        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(null, course);
        assertNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void findByGroupAndCourse_shouldReturnNull_whenInputDoesNotContainCourse() {
        long groupId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        GroupCourse groupCourse = groupCourseRepository.findByGroupAndCourse(group, null);
        assertNull(groupCourse);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroupAndCourse_shouldReturnTrue_whenInputContainsExistingData() {
        long groupId = 1;
        long courseId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isGroupCourseExists = groupCourseRepository.existsByGroupAndCourse(group, course);
        assertTrue(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroupAndCourse_shouldReturnFalse_whenInputContainsNotExistingGroup() {
        long groupId = 100;
        long courseId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isGroupCourseExists = groupCourseRepository.existsByGroupAndCourse(group, course);
        assertFalse(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroupAndCourse_shouldReturnFalse_whenInputContainsNotExistingCourse() {
        long groupId = 1;
        long courseId = 100;
        Group group = groupRepository.findGroupByGroupId(groupId);
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isGroupCourseExists = groupCourseRepository.existsByGroupAndCourse(group, course);
        assertFalse(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroupAndCourse_shouldReturnFalse_whenInputDoesNotContainGroup() {
        long courseId = 1;
        Course course = courseRepository.findCourseByCourseId(courseId);
        boolean isGroupCourseExists = groupCourseRepository.existsByGroupAndCourse(null, course);
        assertFalse(isGroupCourseExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_courses.sql", "/sql/insert_groups_courses.sql"})
    void existsByGroupAndCourse_shouldReturnFalse_whenInputDoesNotContainCourse() {
        long groupId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        boolean isGroupCourseExists = groupCourseRepository.existsByGroupAndCourse(group, null);
        assertFalse(isGroupCourseExists);
    }

}
