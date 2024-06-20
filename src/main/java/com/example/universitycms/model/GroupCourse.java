package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "groups_courses")
@IdClass(GroupCourseId.class)
public class GroupCourse  implements Serializable {


    @Id
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;


    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
