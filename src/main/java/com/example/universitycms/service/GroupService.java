package com.example.universitycms.service;


import com.example.universitycms.exception.*;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.User;
import com.example.universitycms.repository.GroupRepository;
import com.example.universitycms.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public void createGroup(Group group) {

        if(group.getGroupName() == null) {
            throw new GroupNameException("Group must contains name!");
        }

        if(groupRepository.existsByGroupName(group.getGroupName())){
            throw new GroupNotFoundException("Group with this name already exists!");
        }

        groupRepository.save(group);
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }


    @Transactional
    public void removeGroupByGroupId(long groupId) {

        if(!groupRepository.existsByGroupId(groupId)) {
            throw new GroupNotFoundException("Group with this id doesn't exist!");
        }

        groupRepository.deleteByGroupId(groupId);

    }
    public void updateGroup(Group group) {
        Group existingGroup = groupRepository.findGroupByGroupId(group.getGroupId());

        if (existingGroup == null) {
            throw new GroupNotFoundException("This group doesn't exist!");
        }

        if (group.getGroupName() == null) {
            throw new GroupNameException("Group must contains name!");
        }

        if (groupRepository.existsByGroupName(group.getGroupName())) {
            throw new GroupNotFoundException("Group with this name already exists!");
        }

        existingGroup.setGroupName(group.getGroupName());

        groupRepository.save(existingGroup);
    }

    public Group getGroupByGroupId(long groupId) {

        Group group = groupRepository.findGroupByGroupId(groupId);

        if (group == null) {
            throw new GroupNotFoundException("Group with this Id doesn't exists!");
        }

        return group;
    }

    public void assignUserToGroup(long groupId, long userId) {

        Group group = groupRepository.findGroupByGroupId(groupId);
        User user = userRepository.findUserByUserId(userId);

        if(group == null) {
            throw new GroupNotFoundException("Group with this Id doesn't exist!");
        }

        if(user == null) {
            throw new UserNotFoundException("User with this Id doesn't exist!");
        }


        if(group.getUserSet().contains(user)) {
            throw new UserAlreadyAssignedException("User with this Id already assigned!");
        }

        group.getUserSet().add(user);
        user.setGroup(group);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(long groupId, long userId) {

        Group group = groupRepository.findGroupByGroupId(groupId);
        User user = userRepository.findUserByUserId(userId);

        if(user == null) {
            throw new UserNotFoundException("User with this Id doesn't exist!");
        }

        if(group == null) {
            throw new GroupNotFoundException("Group with this Id doesn't exist!");
        }

        if(!group.getUserSet().contains(user)) {
            throw new UserNotAssignedException("User with this Id is not assigned!");
        }

        group.getUserSet().remove(user);
        user.setGroup(null);
        groupRepository.save(group);
    }



    public List<User> getUnassignedUsersToGroup(long groupId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.isUnassignedToGroup(groupId))
                .collect(Collectors.toList());
    }

    public List<User> getAssignedUsersToGroup(long groupId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.isUserInGroup(groupId))
                .collect(Collectors.toList());

    }


}