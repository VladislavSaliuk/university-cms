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

        String name = "Name";

        group = Group.builder()
                .name(name)
                .build();

        groupDTO = GroupDTO.builder()
                .name(name)
                .build();

    }

    @Test
    void createGroup_shouldReturnGroupDTO() {

        when(groupRepository.existsByName(groupDTO.getName()))
                .thenReturn(false);

        when(groupRepository.save(any(Group.class)))
                .thenReturn(group);

        GroupDTO actualGroupDTO = groupService.createGroup(groupDTO);

        assertNotNull(actualGroupDTO);
        assertEquals(groupDTO, actualGroupDTO);

        verify(groupRepository).existsByName(groupDTO.getName());
        verify(groupRepository).save(any(Group.class));

    }


    @Test
        void createGroup_shouldThrowException_whenNameRepeats() {

        when(groupRepository.existsByName(groupDTO.getName()))
                .thenReturn(true);

        GroupException exception = assertThrows(GroupException.class, () -> groupService.createGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getName() + " name already exists!", exception.getMessage());

        verify(groupRepository).existsByName(groupDTO.getName());
        verify(groupRepository, never()).save(any(Group.class));

    }

    @Test
    void updateGroup_shouldReturnGroupDTO() {

        groupDTO.setName("Name 2");

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(groupRepository.existsByName(groupDTO.getName()))
                .thenReturn(false);

        GroupDTO actualGroupDTO = groupService.updateGroup(groupDTO);

        assertNotNull(actualGroupDTO);
        assertEquals(groupDTO, actualGroupDTO);

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository).existsByName(groupDTO.getName());

    }

    @Test
    void updateGroup_shouldThrowException_whenGroupNotFound() {

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.updateGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getGroupId() + " Id not found!", exception.getMessage());

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository, never()).existsByName(groupDTO.getName());

    }
    @Test
        void updateGroup_shouldThrowException_whenNameRepeats() {

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(groupRepository.existsByName(groupDTO.getName()))
                .thenReturn(true);

        GroupException exception = assertThrows(GroupException.class, () -> groupService.updateGroup(groupDTO));

        assertEquals("Group with " + groupDTO.getName() + " name already exists!", exception.getMessage());

        verify(groupRepository).findById(groupDTO.getGroupId());
        verify(groupRepository).existsByName(groupDTO.getName());

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
    void getByName_shouldReturnGroupDTO() {

        String name = "CS-23";

        when(groupRepository.findByName(name))
                .thenReturn(Optional.of(group));

        GroupDTO actualGroupDTO = groupService.getByName(name);

        assertNotNull(actualGroupDTO);
        assertEquals(groupDTO, actualGroupDTO);

        verify(groupRepository).findByName(name);

    }

    @Test
    void getByName_shouldThrowException_whenGroupNotFound() {

        String name = "Name 1";

        when(groupRepository.findByName(name))
                .thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.getByName(name));

        assertEquals("Group with " + name + " name not found!", exception.getMessage());

        verify(groupRepository).findByName(name);

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