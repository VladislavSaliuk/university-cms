package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.User;
import com.example.universitycms.model.UserCourse;
import com.example.universitycms.model.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    UserCourse findByUserAndCourse(User user, Course course);
    boolean existsByUserAndCourse(User user, Course course);

}
