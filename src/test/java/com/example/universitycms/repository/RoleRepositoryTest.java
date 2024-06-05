package com.example.universitycms.repository;

import com.example.universitycms.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findAll_shouldReturnRoleList() {
        List<Role> roleList = roleRepository.findAll();
        assertEquals(3, roleList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleName_shouldReturnRole_whenInputContainsExistingRole() {
        String roleName = "ADMIN";
        Role role = roleRepository.findRoleByRoleName(roleName);
        assertEquals(roleName, role.getRoleName());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleName_shouldReturnNull_whenInputContainsNotExistingRoleName() {
        String roleName = "Test role name";
        Role role = roleRepository.findRoleByRoleName(roleName);
        assertNull(role);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleName_shouldReturnNull_whenInputContainsNull() {
        Role role = roleRepository.findRoleByRoleName(null);
        assertNull(role);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleId_shouldReturnRole_whenInputContainsExistingRoleId() {
        long roleId = 1;
        Role role = roleRepository.findRoleByRoleId(roleId);
        assertEquals(roleId, role.getRoleId());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleId_shouldReturnNull_whenInputContainsNotExistingRoleId() {
        long roleId = 10;
        Role role = roleRepository.findRoleByRoleId(roleId);
        assertNull(role);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void existsByRoleName_shouldReturnTrue_whenInputContainsExistingRoleName(){
        String roleName = "STUDENT";
        boolean isRoleNameExists = roleRepository.existsByRoleName(roleName);
        assertTrue(isRoleNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void existsByRoleName_shouldReturnFalse_whenInputContainsNotExistingRoleName(){
        String roleName = "Test role name";
        boolean isRoleNameExists = roleRepository.existsByRoleName(roleName);
        assertFalse(isRoleNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void existsByRoleName_shouldReturnFalse_whenInputContainsNull(){
        boolean isRoleNameExists = roleRepository.existsByRoleName(null);
        assertFalse(isRoleNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void existsByRoleId_shouldReturnTrue_whenInputContainsExistingRoleId(){
        long roleId = 3;
        boolean isRoleIdExists = roleRepository.existsByRoleId(roleId);
        assertTrue(isRoleIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void existsByRoleId_shouldReturnFalse_whenInputContainsNotExistingRoleId(){
        long roleId = 100;
        boolean isRoleIdExists = roleRepository.existsByRoleId(roleId);
        assertFalse(isRoleIdExists);
    }

}