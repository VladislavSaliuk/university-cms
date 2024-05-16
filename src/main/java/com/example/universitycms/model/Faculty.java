package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "faculties")
@EqualsAndHashCode
@NoArgsConstructor
public class Faculty implements Serializable {

    @Id
    @Column(name = "faculty_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long facultyId;

    @Column(name = "faculty_name" , unique = true)
    private String facultyName;
    public Faculty(String facultyName) {
        this.facultyName = facultyName;
    }
}
