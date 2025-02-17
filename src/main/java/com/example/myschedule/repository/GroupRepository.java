package com.example.myschedule.repository;

import com.example.myschedule.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByGroupName(String groupName);
    boolean existsByGroupName(String groupName);
}