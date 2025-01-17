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
public class StudentDTO {

    @NotNull(message = "Student should contains userId!")
    private long userId;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private Status status;

    private GroupDTO groupDTO;
    public static StudentDTO toStudentDTO(User user) {
        return StudentDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .status(user.getStatus())
                .groupDTO(user.getGroup() != null ? GroupDTO.toGroupDTO(user.getGroup()) : null)
                .build();
    }


}
