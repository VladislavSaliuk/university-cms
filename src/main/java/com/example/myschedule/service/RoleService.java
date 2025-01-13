package com.example.myschedule.service;

import com.example.myschedule.exception.UserRoleException;
import com.example.myschedule.model.Role;
import com.example.myschedule.repository.RoleRepository;
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
