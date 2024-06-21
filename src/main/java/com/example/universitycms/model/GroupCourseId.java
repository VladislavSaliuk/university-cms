package com.example.universitycms.model;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GroupCourseId implements Serializable {

    private Long group;

    private Long course;
}
