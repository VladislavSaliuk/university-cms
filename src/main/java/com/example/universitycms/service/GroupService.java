package com.example.universitycms.service;

import com.example.universitycms.model.Group;
import com.example.universitycms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAll(){
        return groupRepository.findAll();
    }

    public Group getGroupByGroupName(String groupName){

        if(groupName == null){
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!groupRepository.existsByGroupName(groupName)){
            throw new IllegalArgumentException("Group name doesn't exists!");
        }

        return groupRepository.findGroupByGroupName(groupName);
    }

    public Group getGroupByGroupId(long groupId){

        if(!groupRepository.existsByGroupId(groupId)){
            throw new IllegalArgumentException("Group Id doesn't exists!");
        }

        return groupRepository.findGroupByGroupId(groupId);

    }

}
