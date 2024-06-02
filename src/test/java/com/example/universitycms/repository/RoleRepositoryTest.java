package com.example.universitycms.repository;

import com.example.universitycms.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleName_shouldReturnRoleWithCorrectRole_whenInputContainsExistingRole() {
        long roleId = 1;
        Role role = roleRepository.findRoleByRoleId(roleId);
        assertEquals(roleId, role.getRoleId());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql"})
    void findRoleByRoleName_shouldReturnNull_whenInputContainsNotExistingRole() {
        long roleId = 10;
        Role role = roleRepository.findRoleByRoleId(roleId);
        assertNull(role);
    }

}