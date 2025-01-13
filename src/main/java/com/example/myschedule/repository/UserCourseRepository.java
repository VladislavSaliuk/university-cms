package com.example.myschedule.repository;

import com.example.myschedule.model.UserCourse;
import com.example.myschedule.model.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    UserCourse findByUser_UserIdAndCourse_CourseId(long userId, long courseId);
    boolean existsByUser_UserIdAndCourse_CourseId(long userId, long courseId);
    List<UserCourse> findByUser_UserId(long userId);

}
