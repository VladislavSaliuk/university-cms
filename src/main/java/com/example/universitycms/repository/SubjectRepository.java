package com.example.universitycms.repository;

import com.example.universitycms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findSubjectBySubjectName(String subjectName);
    Subject findSubjectBySubjectId(long subjectId);
    boolean existsBySubjectName(String subjectName);
    boolean existsBySubjectId(long subjectId);
}
