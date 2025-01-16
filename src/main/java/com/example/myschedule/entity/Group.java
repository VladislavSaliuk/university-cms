package com.example.myschedule.entity;

import com.example.myschedule.dto.GroupDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
public class Group implements Serializable {

    @Id
    @Column(name = "group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public static Group toGroup(GroupDTO groupDTO) {
        return Group.builder()
                .groupId(groupDTO.getGroupId())
                .name(groupDTO.getName())
                .build();
    }

}