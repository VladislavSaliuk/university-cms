package com.example.universitycms.repository;

import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldInsertUserToDatabase_whenInputContainsUserWithExistingRole() {
        Role role = roleRepository.findRoleByRoleId(3);
        User user = new User("Test user name", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "Test email", role);
        userRepository.save(user);
        assertEquals(11, userRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithExistingNotRole() {
        Role role = roleRepository.findRoleByRoleId(10);
        User user = new User("Test user name", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "Test email", role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithExistingUserName() {
        Role role = roleRepository.findRoleByRoleId(1);
        User user = new User("john_doe", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "Test email", role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithExistingEmail() {
        Role role = roleRepository.findRoleByRoleId(1);
        User user = new User("Test user name", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "alice.jones@example.com", role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, ()-> userRepository.save(null));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithUserNameAsNull() {
        Role role = roleRepository.findRoleByRoleId(3);
        User user = new User(null, "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "Test email", role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithPasswordAsNull() {
        Role role = roleRepository.findRoleByRoleId(1);
        User user = new User("Test user name", null, "Test email", role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithEmailAsNull() {
        Role role = roleRepository.findRoleByRoleId(2);
        User user = new User("Test user name", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", null, role);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void save_shouldThrowException_whenInputContainsUserWithRoleAsNull() {
        User user = new User("Test user name", "$2a$12$C0oG23YPNxAkgNetSRJ/LuEVZt3TT95ulwjrSbAha4qtvvk6hIahS", "Test email", null);
        assertThrows(DataIntegrityViolationException.class, ()-> userRepository.save(user));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findAll_shouldReturnCorrectUserList() {
        List<User> userList = userRepository.findAll();
        assertEquals(10, userList.size());
    }


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserId_shouldReturnUser_whenInputContainsExistingUserId() {
        long expectedUserId = 1;
        User user = userRepository.findUserByUserId(expectedUserId);
        assertEquals(expectedUserId, user.getUserId());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserId_shouldReturnNull_whenInputContainsNotExistingUserId() {
        long userId = 100;
        User user = userRepository.findUserByUserId(userId);
        assertNull(user);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserName_shouldReturnUser_whenInputContainsExistingUserName() {
        String expectedUserName = "bob_brown";
        User user = userRepository.findUserByUserName(expectedUserName);
        assertEquals(expectedUserName, user.getUserName());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserName_shouldReturnNull_whenInputContainsNotExistingUserName() {
        String userName = "Test user name";
        User user = userRepository.findUserByUserName(userName);
        assertNull(user);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserName_shouldReturnNull_whenInputContainsNull() {
        User user = userRepository.findUserByUserName(null);
        assertNull(user);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByUserId_shouldReturnTrue_whenInputContainsExistingUserId() {
        long expectedUserId = 1;
        boolean isUserIdExists = userRepository.existsByUserId(expectedUserId);
        assertTrue(isUserIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByUserId_shouldReturnFalse_whenInputContainsNotExistingUserId() {
        long expectedUserId = 100;
        boolean isUserIdExists = userRepository.existsByUserId(expectedUserId);
        assertFalse(isUserIdExists);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByRole_RoleId_shouldReturnTrue_whenInputContainsExistingRoleId() {
        long roleId = 1;
        boolean isUserExists = userRepository.existsByRole_RoleId(roleId);
        assertTrue(isUserExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByRole_RoleId_shouldReturnFalse_whenInputContainsNotExistingRoleId() {
        long roleId = 100;
        boolean isUserExists = userRepository.existsByRole_RoleId(roleId);
        assertFalse(isUserExists);
    }


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void findUserByUserId_shouldReturnFalse_whenInputContainsNotExistingUserId() {
        long expectedUserId = 100;
        boolean isUserIdExists = userRepository.existsByUserId(expectedUserId);
        assertFalse(isUserIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByUserName_shouldReturnTrue_whenInputContainsExistingUserName() {
        String userName = "frank_foster";
        boolean isUserNameExists = userRepository.existsByUserName(userName);
        assertTrue(isUserNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByUserName_shouldReturnFalse_whenInputContainsNotExistingUserName() {
        String userName = "Test username";
        boolean isUserNameExists = userRepository.existsByUserName(userName);
        assertFalse(isUserNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByUserName_shouldReturnFalse_whenInputContainsNull() {
        boolean isUserNameExists = userRepository.existsByUserName(null);
        assertFalse(isUserNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByEmail_shouldReturnTrue_whenInputContainsExistingEmail() {
        String email = "frank.foster@example.com";
        boolean isEmailExists = userRepository.existsByEmail(email);
        assertTrue(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsNotExistingEmail() {
        String email = "Test E-mail";
        boolean isEmailExists = userRepository.existsByEmail(email);
        assertFalse(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_users.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsNull() {
        boolean isEmailExists = userRepository.existsByEmail(null);
        assertFalse(isEmailExists);
    }

}