package com.example.universitycms.repository;

import com.example.universitycms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherByLogin(String login);
    Teacher findTeacherByPhoneNumber(String phoneNumber);
    Teacher findTeacherByEmail(String email);
    Teacher findTeacherByTeacherId(long teacherId);
    boolean existsByLogin(String login);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByTeacherId(long teacherId);

}
