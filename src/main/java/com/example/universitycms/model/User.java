package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

    @Column(name = "login", nullable = false, unique = true)
    protected String login;
    @Column(name = "password", nullable = false)
    protected String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    protected Role role;


}
