package com.example.universitycms.repository;

import com.example.universitycms.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findAdminByLogin(String login);

}
