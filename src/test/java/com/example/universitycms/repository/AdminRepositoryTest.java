package com.example.universitycms.repository;


import com.example.universitycms.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_admins.sql"})
    void findAdminByLogin_shouldReturnAdminWithCorrectLogin_whenInputContainsExistingLogin() {
        String login = "john_doe";
        Admin admin = adminRepository.findAdminByLogin(login);
        assertEquals(login, admin.getLogin());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_admins.sql"})
    void findAdminByLogin_shouldReturnNull_whenInputContainsNotExistingLogin() {
        String login = "test login";
        Admin admin = adminRepository.findAdminByLogin(login);
        assertNull(admin);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_admins.sql"})
    void findAdminByLogin_shouldReturnNull_whenInputContainsNull() {
        Admin admin = adminRepository.findAdminByLogin(null);
        assertNull(admin);
    }


}
