package com.example.myschedule.repository;

import com.example.myschedule.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByRoleName(String role);
    Role findRoleByRoleId(long roleId);
    boolean existsByRoleName(String roleName);
    boolean existsByRoleId(long roleId);

}
