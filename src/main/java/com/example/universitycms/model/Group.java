package com.example.universitycms.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "groups")
@NoArgsConstructor
public class Group implements Serializable {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;
    @Column(name = "group_name", nullable = false, unique = true)
    private String groupName;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable (
            name = "groups_courses",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courseList;

    public Group(long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
