package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.GroupCourse;
import com.example.universitycms.model.GroupCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupCourseRepository extends JpaRepository<GroupCourse, GroupCourseId> {

    GroupCourse findByGroupAndCourse(Group group, Course course);
    boolean existsByGroupAndCourse(Group group, Course course);

}
