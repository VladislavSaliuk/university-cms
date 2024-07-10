package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courseList;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public User(String userName, String password, String email, Role role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public void setRole(long roleId) {
        this.role = new Role();
        role.setRoleId(roleId);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isUserInGroup(long groupId) {
        return group != null && group.getGroupId() == groupId;
    }

    public boolean isUnassignedToGroup(long groupId) {
        long roleId = this.getRole().getRoleId();
        return (roleId == RoleId.TEACHER.getRoleId()|| roleId == RoleId.TEACHER.getRoleId())
                && (this.getGroup() == null || this.getGroup().getGroupId() != groupId);
    }

}
