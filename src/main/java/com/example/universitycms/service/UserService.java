package com.example.universitycms.service;

import com.example.universitycms.model.Admin;
import com.example.universitycms.model.Student;
import com.example.universitycms.model.Teacher;
import com.example.universitycms.model.User;
import com.example.universitycms.repository.AdminRepository;
import com.example.universitycms.repository.StudentRepository;
import com.example.universitycms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByLogin(username);
        if(user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getLogin())
                    .password(user.getPassword())
                    .roles(user.getRole().getRoleName())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private User getUserByLogin(String login) {

        Teacher teacher = teacherRepository.findTeacherByLogin(login);
        if (teacher != null) {
            return teacher;
        }

        Student student = studentRepository.findStudentByLogin(login);
        if (student != null) {
            return student;
        }

        Admin admin = adminRepository.findAdminByLogin(login);
        if (admin != null) {
            return admin;
        }

        return null;

    }



}
