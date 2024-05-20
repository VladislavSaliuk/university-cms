package com.example.universitycms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "subjects")
@NoArgsConstructor
public class Subject implements Serializable {

    @Id
    @Column(name = "subject_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectId;

    @Column(name = "subject_name", unique = true, nullable = false)
    private String subjectName;

    @Column(name = "subject_description", nullable = false)
    private String subjectDescription;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    public Subject(String subjectName, String subjectDescription, LocalTime time) {
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
        this.time = time;
    }
}
