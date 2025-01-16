package com.example.myschedule.dto;

import com.example.myschedule.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusDTO {

    @NotNull(message = "User should contains Id!")
    private long userId;

    @NotNull(message = "User should contains status!")
    private Status status;

}