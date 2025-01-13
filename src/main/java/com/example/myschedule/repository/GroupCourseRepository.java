package com.example.myschedule.repository;

import com.example.myschedule.model.GroupCourse;
import com.example.myschedule.model.GroupCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupCourseRepository extends JpaRepository<GroupCourse, GroupCourseId> {

    GroupCourse findByGroup_GroupIdAndCourse_CourseId(long groupId, long courseId);
    boolean existsByGroup_GroupIdAndCourse_CourseId(long groupId, long courseId);
    List<GroupCourse> findByGroup_GroupId(long groupId);

}
