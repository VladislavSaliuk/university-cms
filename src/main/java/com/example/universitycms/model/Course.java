package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "courses")
@NoArgsConstructor
@EqualsAndHashCode
public class Course implements Serializable {

    @Id
    @Column(name = "course_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;

    @Column(name = "course_name", unique = true, nullable = false)
    private String courseName;

    @Column(name = "course_description", nullable = false)
    private String courseDescription;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "start_course_time")
    private LocalTime startCourseTime;

    @Column(name = "end_course_time")
    private LocalTime endCourseTime;

    public Course(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
    public Course(String courseName, String courseDescription, String dayOfWeek) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.dayOfWeek = dayOfWeek;
    }

    public Course(String courseName, String courseDescription, String dayOfWeek, LocalTime startCourseTime, LocalTime endCourseTime) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.dayOfWeek = dayOfWeek;
        this.startCourseTime = startCourseTime;
        this.endCourseTime = endCourseTime;
    }
}
