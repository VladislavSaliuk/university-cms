package com.example.myschedule.dto;

import com.example.myschedule.entity.Classroom;
import jakarta.validation.constraints.Max;
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
    @NotNull(message = "Classroom should contains number!")
    private long number;

    private String description;
    public static ClassroomDTO toClassroomDTO(Classroom classroom) {
        return ClassroomDTO.builder()
                .classRoomId(classroom.getClassroomId())
                .number(classroom.getNumber())
                .description(classroom.getDescription())
                .build();
    }

}
