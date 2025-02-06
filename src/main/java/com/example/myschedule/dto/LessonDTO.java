package com.example.myschedule.dto;

import com.example.myschedule.entity.DayOfWeek;
import com.example.myschedule.entity.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private long lessonId;

    private CourseDTO courseDTO;

    private GroupDTO groupDTO;

    @NotNull(message = "Lesson should contains start time!")
    private LocalTime startTime;

    @NotNull(message = "Lesson should contains end time!")
    private LocalTime endTime;

    @NotNull(message = "Lesson should contains day of week!")
    private DayOfWeek dayOfWeek;

    private ClassroomDTO classroomDTO;
    public static LessonDTO toLessonDTO(Lesson lesson) {
        return LessonDTO.builder()
                .lessonId(lesson.getLessonId())
                .courseDTO(CourseDTO.toCourseDTO(lesson.getCourse()))
                .groupDTO(GroupDTO.toGroupDTO(lesson.getGroup()))
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .dayOfWeek(lesson.getDayOfWeek())
                .classroomDTO(ClassroomDTO.toClassroomDTO(lesson.getClassroom()))
                .build();
    }

}
