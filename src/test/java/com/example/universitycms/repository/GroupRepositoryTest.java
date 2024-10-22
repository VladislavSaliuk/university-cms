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
    void save_shouldInsertGroupToDataBase() {
        Group group = new Group("Test group");
        groupRepository.save(group);
        assertEquals(11, groupRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void deleteByGroupId_shouldDeleteGroup_whenGroupIdExists() {
        long groupId = 5;
        groupRepository.deleteByGroupId(groupId);
        assertEquals(9, groupRepository.count());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void deleteByGroupId_shouldNotDeleteGroup_whenGroupIdDoesNotExist() {
        long groupId = 15;
        groupRepository.deleteByGroupId(groupId);
        assertEquals(10, groupRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findAll_shouldReturnGroupList() {
        List<Group> groupList = groupRepository.findAll();
        assertEquals(10, groupList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupId_shouldReturnGroupWithCorrectId_whenInputContainExistingGroup() {
        long groupId = 3;
        Group group = groupRepository.findGroupByGroupId(groupId);
        assertEquals(3, group.getGroupId());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void findGroupByGroupId_shouldReturnNull_whenInputContainsNotExistingGroup() {
        long groupId = 100;
        Group group = groupRepository.findGroupByGroupId(groupId);
        assertNull(group);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupName_shouldReturnTrue_whenGroupExists() {
        String groupName = "EC-24";
        boolean isGroupExists = groupRepository.existsByGroupName(groupName);
        assertTrue(isGroupExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsBtGroupName_shouldReturnFalse_whenGroupDoesNotExist() {
        String groupName = "Test group name";
        boolean isGroupNameExists = groupRepository.existsByGroupName(groupName);
        assertFalse(isGroupNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupId_shouldReturnTrue_whenGroupExists() {
        long groupId = 7;
        boolean isGroupExists = groupRepository.existsByGroupId(groupId);
        assertTrue(isGroupExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
    void existsByGroupId_shouldReturnFalse_whenGroupDoesntExist() {
        long groupId = 100;
        boolean isGroupExists = groupRepository.existsByGroupId(groupId);
        assertFalse(isGroupExists);
    }


}
