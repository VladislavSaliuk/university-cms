package com.example.universitycms.repository;

import com.example.universitycms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    void deleteAll();

    @Transactional
    void deleteCourseByCourseId(long courseId);
    void deleteCourseByCourseName(String courseName);
    Course findCourseByCourseName(String courseName);
    Course findCourseByCourseId(long courseId);
    boolean existsByCourseName(String courseName);
    boolean existsByCourseId(long courseId);
}
