package com.example.universitycms.service;


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

        if(roleName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!roleRepository.existsByRoleName(roleName)) {
            throw new IllegalArgumentException("Role with this name doesn't exist!");
        }

        return roleRepository.findRoleByRoleName(roleName);
    }

    public Role getRoleByRoleId(long roleId) {

        if(!roleRepository.existsByRoleId(roleId)) {
            throw new IllegalArgumentException("Role with this Id doesn't exist!");
        }

        return roleRepository.findRoleByRoleId(roleId);
    }



}
