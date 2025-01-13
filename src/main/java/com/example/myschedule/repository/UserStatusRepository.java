package com.example.myschedule.repository;

import com.example.myschedule.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

    UserStatus findUserStatusByUserStatusId(long userStatusId);
    UserStatus findUserStatusByUserStatusName(String userStatusName);

}
