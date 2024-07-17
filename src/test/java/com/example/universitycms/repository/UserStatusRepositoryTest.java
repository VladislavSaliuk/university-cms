package com.example.universitycms.repository;

import com.example.universitycms.model.UserStatus;
import com.example.universitycms.model.UserStatusId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserStatusRepositoryTest {

    @Autowired
    UserStatusRepository userStatusRepository;


    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_user_statuses.sql"})
    void findUserStatusByUserStatusId_shouldReturnUserStatus_whenInputContainsExistingUserStatusId() {
        UserStatus userStatus = userStatusRepository.findUserStatusByUserStatusId(UserStatusId.ACTIVE.getValue());
        assertNotNull(userStatus);
        assertEquals(UserStatusId.ACTIVE.getValue(), userStatus.getUserStatusId());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_user_statuses.sql"})
    void findUserStatusByUserStatusId_shouldReturnNull_whenInputContainsNotExistingUserStatusId() {
        long userStatusId = 100;
        UserStatus userStatus = userStatusRepository.findUserStatusByUserStatusId(userStatusId);
        assertNull(userStatus);
    }



}
