package com.example.universitycms.repository;

import com.example.universitycms.model.Student;
import com.example.universitycms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByLogin(String login);
    Student findStudentByEmail(String email);
    Student findStudentByStudentId(long studentId);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByStudentId(long studentId);

}
