package com.example.universitycms.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "user_statuses")
@NoArgsConstructor
public class UserStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_status_id", nullable = false)
    private long userStatusId;

    @Column(name = "user_status_name",nullable = false, unique = true)
    private String userStatusName;
    public UserStatus(String userStatusName) {
        this.userStatusName = userStatusName;
    }



}
