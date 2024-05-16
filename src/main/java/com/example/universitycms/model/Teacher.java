package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "teachers")
@NoArgsConstructor
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private long teacherId;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Teacher(String login, String password, String email, String phoneNumber, String firstName, String lastName, int age, Group group, Faculty faculty) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.group = group;
        this.faculty = faculty;
    }
}
