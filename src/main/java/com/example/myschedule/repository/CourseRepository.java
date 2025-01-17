package com.example.myschedule.repository;

import com.example.myschedule.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseName(String courseName);
    boolean existsByCourseName(String courseName);

}
