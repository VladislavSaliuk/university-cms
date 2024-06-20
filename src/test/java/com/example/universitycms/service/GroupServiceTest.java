package com.example.universitycms.service;


import com.example.universitycms.model.Group;
import com.example.universitycms.repository.GroupRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    GroupService groupService;

    @MockBean
    GroupRepository groupRepository;

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
    void getAll_shouldReturnGroupList() {
        when(groupRepository.findAll()).thenReturn(groupList);
        List<Group> actualGroupList = groupService.getAll();
        assertEquals(groupList, actualGroupList);
        verify(groupRepository).findAll();
    }

}
