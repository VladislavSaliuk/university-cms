package com.example.universitycms.service;


import com.example.universitycms.model.Group;
import com.example.universitycms.model.Role;
import com.example.universitycms.repository.RoleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
public class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @MockBean
    RoleRepository roleRepository;

    static List<Role> roleList = new LinkedList<>();

    @BeforeAll
    static void init() {
        roleList.add(new Role("ADMIN"));
        roleList.add(new Role("TEACHER"));
        roleList.add(new Role("STUDENT"));
    }

    @Test
    void getAll_shouldReturnCorrectRoleList() {
        when(roleRepository.findAll()).thenReturn(roleList);
        List<Role> actualRoleList = roleService.getAll();
        assertEquals(roleList, actualRoleList);
        verify(roleRepository).findAll();
    }

    @Test
    void getRoleByRoleName_shouldReturnCorrectRole_whenInputContainsExistingRoleName() {
        String roleName = "ADMIN";
        Role expectedRole = new Role();
        expectedRole.setRoleName(roleList.get(0).getRoleName());
        when(roleRepository.existsByRoleName(roleName)).thenReturn(true);
        when(roleRepository.findRoleByRoleName(roleName)).thenReturn(expectedRole);
        Role actualRole = roleService.getRoleByRoleName(roleName);
        assertTrue(actualRole.equals(expectedRole));
        verify(roleRepository).existsByRoleName(roleName);
        verify(roleRepository).findRoleByRoleName(roleName);
    }

    @Test
    void getRoleByRoleName_shouldThrowException_whenInputContainsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleService.getRoleByRoleName(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(roleRepository,never()).existsByRoleName(null);
        verify(roleRepository,never()).findRoleByRoleName(null);
    }

    @Test
    void getRoleByRoleName_shouldThrowException_whenInputContainsNotExistingRoleName(){
        String roleName = "Test role";
        when(roleRepository.existsByRoleName(roleName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleService.getRoleByRoleName(roleName));
        assertEquals("Role with this name doesn't exist!", exception.getMessage());
        verify(roleRepository).existsByRoleName(roleName);
        verify(roleRepository,never()).findRoleByRoleName(roleName);
    }

    @Test
    void getRoleByRoleId_shouldReturnCorrectRole_whenInputContainsExistingRoleId(){
        List<Role> roleList = LongStream.range(0, 3)
                .mapToObj(roleId -> {
                    Role role = new Role();
                    role.setRoleId(roleId);
                    return role;
                })
                .collect(Collectors.toList());
        long roleId = 1;
        Role expectedRole = new Role();
        expectedRole.setRoleId(roleList.get(0).getRoleId());
        when(roleRepository.existsByRoleId(roleId)).thenReturn(true);
        when(roleRepository.findRoleByRoleId(roleId)).thenReturn(expectedRole);
        Role actualRole = roleService.getRoleByRoleId(roleId);
        assertTrue(actualRole.equals(expectedRole));
        verify(roleRepository).existsByRoleId(roleId);
        verify(roleRepository).findRoleByRoleId(roleId);
    }

    @Test
    void getRoleByRoleId_shouldThrowException_whenInputContainsNotExistingRoleId(){
        long roleId = 100;
        when(roleRepository.existsByRoleId(roleId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleService.getRoleByRoleId(roleId));
        assertEquals("Role with this Id doesn't exist!", exception.getMessage());
        verify(roleRepository).existsByRoleId(roleId);
        verify(roleRepository,never()).findRoleByRoleId(roleId);
    }

}
