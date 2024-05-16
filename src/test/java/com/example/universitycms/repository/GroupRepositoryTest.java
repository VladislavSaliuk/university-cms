package com.example.universitycms.repository;

import com.example.universitycms.model.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findAll_shouldReturnCorrectGroupList(){
        List<Group> groupList = groupRepository.findAll();
        assertEquals(10, groupList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupName_shouldReturnGroupWithCorrectName_whenInputContainsExistingGroupName(){
        String groupName = "EI-21";
        Group group = groupRepository.findGroupByGroupName(groupName);
        assertTrue(group.getGroupName().equals(groupName));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupName_shouldReturnNull_whenInputContainsNotExistingGroupName(){
        String groupName = "Test group";
        Group group = groupRepository.findGroupByGroupName(groupName);
        assertNull(group);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupName_shouldReturnNull_whenInputContainsNull(){
        Group group = groupRepository.findGroupByGroupName(null);
        assertNull(group);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupId_shouldReturnGroupWithCorrectId_whenInputContainsExistingGroupName(){
        long groupId = 1;
        Group group = groupRepository.findGroupByGroupId(groupId);
        assertTrue(groupId == group.getGroupId());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupId_shouldReturnNull_whenInputContainsNotExistingGroupName(){
        long groupId = 11;
        Group group = groupRepository.findGroupByGroupId(groupId);
        assertNull(group);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupName_shouldReturnTrue_whenInputContainsExistingGroupName(){
        String groupName = "EI-21";
        boolean isGroupNameExists = groupRepository.existsByGroupName(groupName);
        assertTrue(isGroupNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupName_shouldReturnFalse_whenInputContainsNotExistingGroupName(){
        String groupName = "Test group";
        boolean isGroupNameExists = groupRepository.existsByGroupName(groupName);
        assertFalse(isGroupNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupName_shouldReturnNullPointerException_whenInputContainsNull(){
        boolean isGroupNameExists = groupRepository.existsByGroupName(null);
        assertFalse(isGroupNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupId_shouldReturnTrue_whenInputContainsExistingGroupId(){
        long groupId = 5;
        boolean isGroupIdExists = groupRepository.existsByGroupId(groupId);
        assertTrue(isGroupIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupId_shouldReturnFalse_whenInputContainsNotExistingGroupId(){
        long groupId = 100;
        boolean isGroupIdExists = groupRepository.existsByGroupId(groupId);
        assertFalse(isGroupIdExists);
    }



}
