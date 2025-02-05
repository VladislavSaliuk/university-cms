package com.example.myschedule.dto;

import com.example.myschedule.entity.Classroom;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDTO {

    private long classRoomId;

    @Min(value = 0L, message = "Number cannot be negativ!")
    private long classroomNumber;

    private String classroomDescription;
    public static ClassroomDTO toClassroomDTO(Classroom classroom) {
        return ClassroomDTO.builder()
                .classRoomId(classroom.getClassroomId())
                .classroomNumber(classroom.getClassroomNumber())
                .classroomDescription(classroom.getClassroomDescription())
                .build();
    }

}
