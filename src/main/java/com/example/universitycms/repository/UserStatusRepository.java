package com.example.universitycms.repository;

import com.example.universitycms.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

    UserStatus findUserStatusByUserStatusId(long userStatusId);
    UserStatus findUserStatusByUserStatusName(String userStatusName);

}
