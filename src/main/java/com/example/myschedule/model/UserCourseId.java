package com.example.myschedule.model;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserCourseId implements Serializable {

    private Long user;

    private Long course;


}
