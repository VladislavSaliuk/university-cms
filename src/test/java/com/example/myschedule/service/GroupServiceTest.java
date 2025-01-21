package com.example.myschedule.service;
import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.exception.GroupException;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @InjectMocks
    GroupService groupService;
    @Mock
    GroupRepository groupRepository;
    Group group;
    GroupDTO groupDTO;

    @BeforeEach
    void setUp(){

        String groupName = "Group name";

        group = Group.builder()
                .groupName(groupName)
                .build();

        groupDTO = GroupDTO.builder()
                .groupName(groupName)
                .build();

    }

    @Test
    void createGroup_shouldSaveGroup() {

        when(groupRepository.existsByGroupName(groupDTO.getGroupName()))
                .thenReturn(false);

        when(groupRepository.save(any(Group.class)))
                .thenReturn(group);

        groupService.createGroup(groupDTO);

        verify(groupRepository).existsByGroupName(groupDTO.getGroupName());
        verify(groupRepository).save(any(Group.class));

    }


    @Test
        void createGroup_shouldThrowException_whenGroupNameRepeats() {

        when(groupRepository.existsByGroupName(groupDTO.getGroupName()))
                .thenReturn(true);

        GroupException exception = assertThrows(GroupException.class, () -> groupService.createGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getGroupName() + " name already exists!", exception.getMessage());

        verify(groupRepository).existsByGroupName(groupDTO.getGroupName());
        verify(groupRepository, never()).save(any(Group.class));

    }

    @Test
    void updateGroup_shouldUpdateGroup() {

        groupDTO.setGroupName("Name 2");

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(groupRepository.existsByGroupName(groupDTO.getGroupName()))
                .thenReturn(false);

        groupService.updateGroup(groupDTO);

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository).existsByGroupName(groupDTO.getGroupName());

    }

    @Test
    void updateGroup_shouldThrowException_whenGroupNotFound() {

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.updateGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getGroupId() + " Id not found!", exception.getMessage());

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository, never()).existsByGroupName(groupDTO.getGroupName());

    }
    @Test
        void updateGroup_shouldThrowException_whenGroupNameRepeats() {

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(groupRepository.existsByGroupName(groupDTO.getGroupName()))
                .thenReturn(true);

        GroupException exception = assertThrows(GroupException.class, () -> groupService.updateGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getGroupName() + " name already exists!", exception.getMessage());

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository).existsByGroupName(groupDTO.getGroupName());

    }


    @Test
    void getAll_shouldReturnGroupDTOList() {

        when(groupRepository.findAll())
                .thenReturn(List.of(group));

        List<GroupDTO> groupDTOList = groupService.getAll();

        assertNotNull(groupDTOList);
        assertFalse(groupDTOList.isEmpty());
        assertEquals(1, groupDTOList.size());

        verify(groupRepository).findAll();

    }

    @Test
    void getById_shouldReturnGroupDTO() {

        long groupId = 1L;

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        GroupDTO actualGroupDTO = groupService.getById(groupId);

        assertNotNull(actualGroupDTO);
        assertEquals(groupDTO, actualGroupDTO);


        verify(groupRepository).findById(groupId);

    }

    @Test
    void getById_shouldThrowException_whenGroupNotFound() {

        long groupId = 100L;

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.getById(groupId));

        assertEquals("Group with " + groupId + " Id not found!", exception.getMessage());

        verify(groupRepository).findById(groupId);

    }

    @Test
    void getByGroupName_shouldReturnGroupDTO() {

        String groupName = "CS-23";

        when(groupRepository.findByGroupName(groupName))
                .thenReturn(Optional.of(group));

        GroupDTO actualGroupDTO = groupService.getByGroupName(groupName);

        assertNotNull(actualGroupDTO);
        assertEquals(groupDTO, actualGroupDTO);

        verify(groupRepository).findByGroupName(groupName);

    }

    @Test
    void getByGroupName_shouldThrowException_whenGroupNotFound() {

        String groupName = "Name 1";

        when(groupRepository.findByGroupName(groupName))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.getByGroupName(groupName));

        assertEquals("Group with " + groupName + " name not found!", exception.getMessage());

        verify(groupRepository).findByGroupName(groupName);

    }

    @Test
    void removeById_shouldDeleteGroup() {

        long groupId = 1L;

        when(groupRepository.existsById(groupId))
                .thenReturn(true);

        doNothing().when(groupRepository).deleteById(groupId);

        groupService.removeById(groupId);

        verify(groupRepository).existsById(groupId);
        verify(groupRepository).deleteById(groupId);

    }

    @Test
    void removeById_shouldThrowException_whenGroupNotFound() {

        long groupId = 100L;

        when(groupRepository.existsById(groupId))
                .thenReturn(false);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.removeById(groupId));

        assertEquals("Group with " + groupId + " Id not found!", exception.getMessage());

        verify(groupRepository).existsById(groupId);
        verify(groupRepository, never()).deleteById(groupId);

    }

}