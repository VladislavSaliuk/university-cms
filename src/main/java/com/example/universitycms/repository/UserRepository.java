package com.example.universitycms.repository;


import com.example.universitycms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUserId(long userId);
    User findUserByUserName(String userName);
    User findUserByEmail(String email);
    boolean existsByUserId(long userId);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

}
