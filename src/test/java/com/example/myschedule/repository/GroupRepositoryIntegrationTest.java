package com.example.myschedule.repository;

import com.example.myschedule.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql"})
public class GroupRepositoryIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    GroupRepository groupRepository;
    Group group;

    @BeforeEach
    void setUp() {

        String groupName = "Group name";

        group = Group.builder()
                .groupName(groupName)
                .build();

    }

    @Test
    void save_shouldPersistGroup() {
        groupRepository.save(group);
        assertEquals(11, groupRepository.count());
    }
    @Test
    void save_shouldThrowException_whenNameIsNull() {
            group.setGroupName(null);
            DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> groupRepository.save(group));
            assertEquals(10, groupRepository.count());
    }
    @ParameterizedTest
    @ValueSource(strings = {"TB-23", "TB-24", "MK-23", "MK-24", "FI-23", "FI-24", "CS-23", "CS-24", "BI-23", "BI-24"})
    void save_shouldThrowException_whenGroupNameRepeats(String name) {
        group.setGroupName(name);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> groupRepository.save(group));
        assertEquals(10, groupRepository.count());
    }

    @Test
    void findAll_shouldReturnGroupList() {
        List<Group> groupList = groupRepository.findAll();
        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
        assertEquals(10, groupList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnGroup(long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        assertTrue(optionalGroup.isPresent());
    }
    @Test
    void findById_shouldThrowException_whenGroupNotFound() {
        long groupId = 100L;
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        assertTrue(optionalGroup.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TB-23", "TB-24", "MK-23", "MK-24", "FI-23", "FI-24", "CS-23", "CS-24", "BI-23", "BI-24"})
    void findByGroupName_shouldReturnGroup(String groupName) {
        Optional<Group> optionalGroup = groupRepository.findByGroupName(groupName);
        assertTrue(optionalGroup.isPresent());
    }

    @Test
    void findByGroupName_shouldThrowException_whenGroupNotFound() {
        Optional<Group> optionalGroup = groupRepository.findByGroupName(group.getGroupName());
        assertTrue(optionalGroup.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldRemoveGroup(long groupId) {
        groupRepository.deleteById(groupId);
        assertEquals(9, groupRepository.count());
    }

    @Test
    void deleteById_shouldNotRemoveGroup_whenGroupNotFound() {
        long groupId = 100L;
        groupRepository.deleteById(groupId);
        assertEquals(10, groupRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void existsById_shouldReturnTrue(long groupId) {
        boolean isGroupExists = groupRepository.existsById(groupId);
        assertTrue(isGroupExists);
    }

    @Test
    void existsById_shouldReturnFalse_whenGroupNotExist() {
        long groupId = 100L;
        boolean isGroupExists = groupRepository.existsById(groupId);
        assertFalse(isGroupExists);
    }

    @ParameterizedTest
    @ValueSource(strings = {"TB-23", "TB-24", "MK-23", "MK-24", "FI-23", "FI-24", "CS-23", "CS-24", "BI-23", "BI-24"})
    void existsByGroupName_shouldReturnTrue(String groupName) {
        boolean isGroupExists = groupRepository.existsByGroupName(groupName);
        assertTrue(isGroupExists);
    }
    @Test
    void existsByGroupName_shouldReturnFalse_whenGroupNotExist() {
        boolean isGroupExists = groupRepository.existsByGroupName(group.getGroupName());
        assertFalse(isGroupExists);
    }

}