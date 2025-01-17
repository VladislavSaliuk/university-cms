package com.example.myschedule.dto;

import com.example.myschedule.entity.Course;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    @NotNull(message = "Course should contains Id!")
    private long courseId;

    @NotNull(message = "Course should contains name!")
    private String courseName;

    private String courseDescription;

    private TeacherDTO teacherDTO;
    public static CourseDTO toCourseDTO(Course course) {
        return CourseDTO.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .teacherDTO(course.getUser() != null ? TeacherDTO.toTeacherDTO(course.getUser()) : null)
                .build();
    }

}
