package com.example.universitycms.service;

import com.example.universitycms.model.Course;
import com.example.universitycms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getSubjectBySubjectName(String subjectName) {

        if(subjectName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!courseRepository.existsByCourseName(subjectName)) {
            throw new IllegalArgumentException("Course name doesn't exists!");
        }

        return courseRepository.findCourseByCourseName(subjectName);
    }

    public Course getSubjectBySubjectId(long subjectId) {

        if(!courseRepository.existsByCourseId(subjectId)) {
            throw new IllegalArgumentException("Course Id doesn't exists!");
        }

        return courseRepository.findCourseByCourseId(subjectId);
    }

}
