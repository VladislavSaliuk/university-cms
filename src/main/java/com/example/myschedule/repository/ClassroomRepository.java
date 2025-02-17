package com.example.myschedule.repository;

import com.example.myschedule.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    Optional<Classroom> findByClassroomNumber(long number);
    boolean existsByClassroomNumber(long number);
}
