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


    public List<Group> getAll() {
        return groupRepository.findAll();
    }

}
