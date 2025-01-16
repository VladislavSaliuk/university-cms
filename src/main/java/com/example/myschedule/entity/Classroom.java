package com.example.myschedule.entity;

import com.example.myschedule.dto.ClassroomDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "classrooms")
public class Classroom implements Serializable {

    @Id
    @Column(name = "classroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomId;

    @Column(name = "description")
    private String description;

    @Column(name = "number", nullable = false, unique = true)
    private long number;

    public static Classroom toClassroom(ClassroomDTO classroomDTO) {
        return Classroom.builder()
                .classroomId(classroomDTO.getClassRoomId())
                .description(classroomDTO.getDescription())
                .number(classroomDTO.getNumber())
                .build();
    }

}
