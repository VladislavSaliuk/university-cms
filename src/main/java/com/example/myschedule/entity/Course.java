package com.example.myschedule.entity;


import com.example.myschedule.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course implements Serializable {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;

    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    @Column(name = "course_description")
    private String courseDescription;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;

}