package com.example.universitycms.service;

import com.example.universitycms.exception.UserRoleException;
import com.example.universitycms.model.Role;
import com.example.universitycms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getRoleByRoleName(String roleName) {

        Role role = roleRepository.findRoleByRoleName(roleName);

        if(role == null) {
            throw new UserRoleException("User role with this name not found!");
        }

        return role;

    }


}
