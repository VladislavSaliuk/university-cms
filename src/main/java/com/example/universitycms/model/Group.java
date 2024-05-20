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
@Table(name = "groups")
@NoArgsConstructor
@EqualsAndHashCode
public class Group implements Serializable{
    @Id
    @Column(name = "group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;

    @Column(name = "group_name", unique = true, nullable = false)
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }
}
