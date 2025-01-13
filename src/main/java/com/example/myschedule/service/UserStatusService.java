package com.example.myschedule.service;

import com.example.myschedule.exception.UserStatusException;
import com.example.myschedule.model.UserStatus;
import com.example.myschedule.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;



    public UserStatus getUserStatusByUserStatusId(long userStatusId) {

        UserStatus userStatus = userStatusRepository.findUserStatusByUserStatusId(userStatusId);

        if(userStatus == null) {
            throw new UserStatusException("User status with this Id not found!");
        }

        return userStatus;
    }

    public UserStatus getUserStatusByUserStatusName(String userStatusName) {

        UserStatus userStatus = userStatusRepository.findUserStatusByUserStatusName(userStatusName);

        if(userStatus == null) {
            throw new UserStatusException("User status with this name not found!");
        }

        return userStatus;
    }


}
