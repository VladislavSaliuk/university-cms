package com.example.universitycms.repository;

import com.example.universitycms.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findGroupByGroupId(long groupId);
    boolean existsByGroupId(long groupId);

}
