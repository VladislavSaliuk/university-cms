package com.example.myschedule.dto;

import com.example.myschedule.entity.Group;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private long groupId;

    @NotNull(message = "Group should contains name!")
    private String groupName;
    public static GroupDTO toGroupDTO(Group group) {
        return GroupDTO.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .build();
    }

}
