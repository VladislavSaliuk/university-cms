package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findCourseByCourseName(String courseName);
    Course findCourseByCourseId(long courseId);
    boolean existsByCourseName(String courseName);
    boolean existsByCourseId(long courseId);
}
