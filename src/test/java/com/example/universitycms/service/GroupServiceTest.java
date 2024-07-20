package com.example.universitycms.service;


import com.example.universitycms.exception.*;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import com.example.universitycms.repository.GroupRepository;
import com.example.universitycms.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    GroupService groupService;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    UserRepository userRepository;

    static List<Group> groupList = new LinkedList<>();

    @BeforeAll
    static void init() {
        groupList.add(new Group("Test group 1"));
        groupList.add(new Group("Test group 2"));
        groupList.add(new Group("Test group 3"));
        groupList.add(new Group("Test group 4"));
        groupList.add(new Group("Test group 5"));
    }


    @Test
    void createGroup_shouldInsertGroupToDatabase_whenGroupNameDoesntExist() {
        Group group = new Group("Test group name");
        when(groupRepository.existsByGroupName(group.getGroupName()))
                .thenReturn(false);
        groupService.createGroup(group);
        verify(groupRepository).existsByGroupName(group.getGroupName());
        verify(groupRepository).save(group);
    }

    @Test
    void createGroup_shouldThrowException_whenGroupNameDoesNotExist() {
        Group group = new Group();
        GroupNameException exception = assertThrows(GroupNameException.class, () -> groupService.createGroup(group));
        assertEquals(exception.getMessage(), "Group must contains name!");
        verify(groupRepository, never()).existsByGroupName(group.getGroupName());
        verify(groupRepository, never()).save(group);
    }

    @Test
    void createGroup_shouldThrowException_whenGroupNameExists() {
        Group group = new Group("Test group name 1");
        when(groupRepository.existsByGroupName(group.getGroupName()))
                .thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.createGroup(group));
        assertEquals(exception.getMessage(),"Group with this name already exists!");
        verify(groupRepository).existsByGroupName(group.getGroupName());
        verify(groupRepository, never()).save(group);
    }

    @Test
    void removeGroupByGroupId_shouldRemoveGroup_whenInputContainsExitingGroupId() {
        long groupId = 2;
        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(true);
        groupService.removeGroupByGroupId(groupId);
        verify(groupRepository).existsByGroupId(groupId);
        verify(groupRepository).deleteByGroupId(groupId);
    }

    @Test
    void removeGroupByGroupId_shouldNotRemoveGroup_whenInputContainsNotExistingGroupId() {
        long groupId = 100;
        when(groupRepository.existsByGroupId(groupId))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () ->groupService.removeGroupByGroupId(groupId));
        assertEquals(exception.getMessage(),"Group with this id doesn't exist!");
        verify(groupRepository).existsByGroupId(groupId);
        verify(groupRepository,never()).deleteByGroupId(groupId);
    }
    @Test
    void updateGroup_shouldUpdateGroup_whenInputContainsExistingGroup() {
        long groupId = 1;

        Group existingGroup = new Group();
        existingGroup.setGroupId(groupId);
        existingGroup.setGroupName("PH-24");

        Group updatedGroup = new Group();
        updatedGroup.setGroupId(groupId);
        updatedGroup.setGroupName("Test group name");

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(existingGroup);

        when(groupRepository.existsByGroupName(existingGroup.getGroupName()))
                .thenReturn(true);

        groupService.updateGroup(updatedGroup);

        verify(groupRepository).findGroupByGroupId(groupId);
        verify(groupRepository).existsByGroupName(existingGroup.getGroupName());
        verify(groupRepository).save(existingGroup);
    }

    @Test
    void updateGroup_shouldThrowException_whenInputContainsNotExistingGroup() {
        long groupId = 100;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("Test group name");

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(null)
                .thenThrow(IllegalArgumentException.class);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.updateGroup(group));
        assertEquals(exception.getMessage(), "This group doesn't exist!");

        verify(groupRepository).findGroupByGroupId(groupId);
        verify(groupRepository,never()).existsByGroupName(group.getGroupName());
        verify(groupRepository,never()).save(group);
    }

    @Test
    void updateGroup_shouldThrowException_whenUpdatedGroupNameContainsNull() {
        long groupId = 1;

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName("EN-22");

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(group);

        group.setGroupName(null);

        GroupNameException exception = assertThrows(GroupNameException.class, () -> groupService.updateGroup(group));

        assertEquals(exception.getMessage(), "Group must contains name!");
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(groupRepository, never()).existsByGroupName(group.getGroupName());
        verify(groupRepository, never()).save(group);
    }

    @Test
    void updateGroup_shouldThrowException_whenUpdatedGroupContainsExistingGroupName() {
        long groupId = 1;

        Group existingGroup = new Group();
        existingGroup.setGroupId(groupId);
        existingGroup.setGroupName("SO-24");

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(existingGroup);

        Group updatedGroup = new Group();
        updatedGroup.setGroupId(groupId);
        updatedGroup.setGroupName("BI-23");

        when(groupRepository.existsByGroupName(updatedGroup.getGroupName())).thenReturn(true);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.updateGroup(updatedGroup));

        assertEquals("Group with this name already exists!", exception.getMessage());
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(groupRepository).existsByGroupName(updatedGroup.getGroupName());
        verify(groupRepository, never()).save(updatedGroup);
    }

    @Test
    void getAll_shouldReturnGroupList() {
        when(groupRepository.findAll()).thenReturn(groupList);
        List<Group> actualGroupList = groupService.getAll();
        assertEquals(groupList, actualGroupList);
        verify(groupRepository).findAll();
    }

    @Test
    void getGroupByGroupId_shouldReturnGroup_whenInputContainsGroupWithExistingName() {
        List<Group> groupList = LongStream.range(0, 10)
                .mapToObj(groupId -> {
                    Group group = new Group();
                    group.setGroupId(groupId);
                    return group;
                })
                .collect(Collectors.toList());
        long groupId = 1;

        Group expectedGroup = groupList.get(0);

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(expectedGroup);
        Group actualGroup = groupService.getGroupByGroupId(groupId);
        assertEquals(expectedGroup, actualGroup);

        verify(groupRepository).findGroupByGroupId(groupId);
    }

    @Test
    void getGroupByGroupId_shouldThrowException_whenInputContainsGroupWithNotExistingGroupId() {
        long groupId = 100;

        when(groupRepository.findGroupByGroupId(groupId)).thenReturn(null);
        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.getGroupByGroupId(groupId));
        assertEquals("Group with this Id doesn't exists!",exception.getMessage());

        verify(groupRepository).findGroupByGroupId(groupId);
    }


    @Test
    void assignUserToGroup_shouldInsertGroupIdToUserTable_whenInputContainsExistingData() {

        long groupId = 3;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        groupService.assignUserToGroup(groupId, userId);
        assertEquals(group.getUserSet().size(), 1);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository).save(group);
    }

    @Test
    void assignUserToGroup_shouldThrowException_whenInputContainsNotExistingGroupId() {

        long groupId = 100;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(null);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.assignUserToGroup(groupId, userId));

        assertEquals("Group with this Id doesn't exist!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 0);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);
    }

    @Test
    void assignUserToGroup_shouldThrowException_whenInputContainsNotExistingUserId() {

        long groupId = 3;
        long userId = 100;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> groupService.assignUserToGroup(groupId, userId));

        assertEquals("User with this Id doesn't exist!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 0);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);
    }

    @Test
    void assignUserToGroup_shouldThrowException_whenInputContainsAssignedUserId() {

        long groupId = 3;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        group.getUserSet().add(user);

        UserAlreadyAssignedException exception = assertThrows(UserAlreadyAssignedException.class, () -> groupService.assignUserToGroup(groupId, groupId));

        assertEquals("User with this Id already assigned!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 1);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);

    }

    @Test
    void removeUserFromGroup_shouldDeleteGroupIdFromUser_whenInputContainsExistingData() {

        long groupId = 3;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        group.getUserSet().add(user);

        groupService.removeUserFromGroup(groupId, userId);

        assertEquals(group.getUserSet().size(), 0);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(groupRepository).save(group);
    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenInputContainsNotExisting() {

        long groupId = 100;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(null);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        group.getUserSet().add(user);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.removeUserFromGroup(groupId, userId));

        assertEquals("Group with this Id doesn't exist!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 1);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);

    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenInputContainsAssignedUserId() {

        long groupId = 3;
        long userId = 100;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        group.getUserSet().add(user);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> groupService.removeUserFromGroup(groupId, userId));

        assertEquals("User with this Id doesn't exist!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 1);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);
    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenInputContainsNotAssignedUserId() {

        long groupId = 3;
        long userId = 3;

        Group group = new Group();
        group.setGroupId(groupId);

        User user = new User();
        user.setUserId(userId);

        when(groupRepository.findGroupByGroupId(groupId))
                .thenReturn(group);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        UserNotAssignedException exception = assertThrows(UserNotAssignedException.class, () -> groupService.removeUserFromGroup(groupId, userId));

        assertEquals("User with this Id is not assigned!", exception.getMessage());
        assertEquals(group.getUserSet().size(), 0);
        verify(groupRepository).findGroupByGroupId(groupId);
        verify(userRepository).findUserByUserId(userId);
        verify(groupRepository, never()).save(group);

    }

    @Test
    void getUnassignedUsersToGroup_shouldReturnUserList_whenInputContainsExistingGroupId() {

        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("Test role");

        long groupId = 1;

        Group group = new Group();
        group.setGroupId(groupId);

        Set<User> userSet = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setRole(role);
                    user.setGroup(group);
                    return user;
                })
                .collect(Collectors.toSet());

        group.setUserSet(userSet);

        when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        List<User> unAssignedUserList = groupService.getUnassignedUsersToGroup(groupId);
        assertEquals(unAssignedUserList.size(), 0);
        verify(userRepository).findAll();
    }

    @Test
    void getUnassignedUsersToGroup_shouldReturnEmptyList_whenInputContainsNotExistingGroupId() {

        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("Test role");

        long groupId = 1;

        Group group = new Group();
        group.setGroupId(groupId);

        Set<User> userSet = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setRole(role);
                    user.setGroup(group);
                    return user;
                })
                .collect(Collectors.toSet());

        group.setUserSet(userSet);

        when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        List<User> unAssignedUserList = groupService.getUnassignedUsersToGroup(100);
        assertEquals(unAssignedUserList.size(), 10);
        verify(userRepository).findAll();
    }

    @Test
    void getAssignedUsersToGroup_shouldReturnUserList_whenInputContainsExistingGroupId() {

        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("Test role");

        long groupId = 1;

        Group group = new Group();
        group.setGroupId(groupId);

        Set<User> userSet = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setRole(role);
                    user.setGroup(group);
                    return user;
                })
                .collect(Collectors.toSet());

        group.setUserSet(userSet);

        when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        List<User> unAssignedUserList = groupService.getAssignedUsersToGroup(groupId);
        assertEquals(unAssignedUserList.size(), 10);
        verify(userRepository).findAll();
    }

    @Test
    void getAssignedUsersToGroup_shouldReturnEmptyList_whenInputContainsNotExistingGroupId() {

        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("Test role");

        long groupId = 1;

        Group group = new Group();
        group.setGroupId(groupId);

        Set<User> userSet = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setRole(role);
                    user.setGroup(group);
                    return user;
                })
                .collect(Collectors.toSet());

        group.setUserSet(userSet);

        when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        List<User> unAssignedUserList = groupService.getAssignedUsersToGroup(100);
        assertEquals(unAssignedUserList.size(), 0);
        verify(userRepository).findAll();
    }


}
