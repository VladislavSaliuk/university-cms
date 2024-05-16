package com.example.universitycms.service;

import com.example.universitycms.model.Group;
import com.example.universitycms.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    GroupService groupService;

    @MockBean
    GroupRepository groupRepository;

    @Test
    void findAll_shouldReturnCorrectGroupList(){
        List<Group> expectedGroupList = new LinkedList<>();
        expectedGroupList.add(new Group("Test group 1"));
        expectedGroupList.add(new Group("Test group 2"));
        expectedGroupList.add(new Group("Test group 3"));
        expectedGroupList.add(new Group("Test group 4"));
        expectedGroupList.add(new Group("Test group 5"));
        expectedGroupList.add(new Group("Test group 6"));
        when(groupRepository.findAll()).thenReturn(expectedGroupList);
        List<Group> actualGroupList = groupService.findAll();
        assertEquals(expectedGroupList,actualGroupList);
        verify(groupRepository).findAll();
    }

    @Test
    void findGroupByGroupName_shouldReturnCorrectGroup_whenInputContainsExistingGroupName(){
        List<Group> groupList = new LinkedList<>();
        groupList.add(new Group("Test group 1"));
        groupList.add(new Group("Test group 2"));
        groupList.add(new Group("Test group 3"));
        groupList.add(new Group("Test group 4"));
        groupList.add(new Group("Test group 5"));
        groupList.add(new Group("Test group 6"));
        String groupName = "Test group 1";
        Group expetedGroup = new Group();
        expetedGroup.setGroupName(groupList.get(0).getGroupName());
        when(groupRepository.existsByGroupName(groupName)).thenReturn(true);
        when(groupRepository.findGroupByGroupName(groupName)).thenReturn(expetedGroup);
        Group actualGroup = groupService.findGroupByGroupName(groupName);
        assertTrue(actualGroup.equals(expetedGroup));
        verify(groupRepository).existsByGroupName(groupName);
        verify(groupRepository).findGroupByGroupName(groupName);
    }

    @Test
    void findGroupByGroupName_shouldThrowIllegalArgumentException_whenInputContainsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupService.findGroupByGroupName(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(groupRepository,never()).existsByGroupName(null);
        verify(groupRepository,never()).findGroupByGroupName(null);
    }

    @Test
    void findGroupByGroupName_shouldThrowIllegalArgumentException_whenInputContainsNotExistingGroupName(){
        String groupName = "Test group 10";
        when(groupRepository.existsByGroupName(groupName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupService.findGroupByGroupName(groupName));
        assertEquals("Group name doesn't exists!", exception.getMessage());
        verify(groupRepository).existsByGroupName(groupName);
        verify(groupRepository,never()).findGroupByGroupName(groupName);
    }

    @Test
    void findGroupByGroupId_shouldReturnCorrectGroup_whenInputContainsExistingGroupId(){
        List<Group> groupList = LongStream.range(0, 10)
                .mapToObj(groupId -> {
                    Group group = new Group();
                    group.setGroupId(groupId);
                    return group;
                })
                .collect(Collectors.toList());
        long groupId = 1;
        Group expetedGroup = new Group();
        expetedGroup.setGroupId(groupList.get(0).getGroupId());
        when(groupRepository.existsByGroupId(groupId)).thenReturn(true);
        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(expetedGroup);
        Group actualGroup = groupService.findGroupByGroupId(groupId);
        assertTrue(actualGroup.equals(expetedGroup));
        verify(groupRepository).existsByGroupId(groupId);
        verify(groupRepository).findGroupByGroupId(groupId);
    }

    @Test
    void findGroupByGroupId_shouldThrowIllegalArgumentException_whenInputContiansNotExistingGroupId(){
        long groupId = 100;
        when(groupRepository.existsByGroupId(groupId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> groupService.findGroupByGroupId(groupId));
        assertEquals("Group Id doesn't exists!", exception.getMessage());
        verify(groupRepository).existsByGroupId(groupId);
        verify(groupRepository,never()).findGroupByGroupId(groupId);
    }

}