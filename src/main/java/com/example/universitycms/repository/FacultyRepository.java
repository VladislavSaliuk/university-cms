package com.example.universitycms.repository;

import com.example.universitycms.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findFacultyByFacultyName(String facultyName);
    Faculty findFacultyByFacultyId(long facultyId);
    boolean existsByFacultyName(String facultyName);
    boolean existsByFacultyId(long facultyId);

}
