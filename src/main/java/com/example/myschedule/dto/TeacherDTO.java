package com.example.myschedule.dto;

import com.example.myschedule.entity.Status;
import com.example.myschedule.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {

    @NotNull(message = "Teacher should contains userId!")
    private long userId;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private Status status;
    public static TeacherDTO toTeacherDTO(User user) {
        return TeacherDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .status(user.getStatus())
                .build();
    }

}