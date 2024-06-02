package com.example.universitycms.service;

import com.example.universitycms.model.Admin;
import com.example.universitycms.model.Student;
import com.example.universitycms.model.Teacher;
import com.example.universitycms.repository.AdminRepository;
import com.example.universitycms.repository.StudentRepository;
import com.example.universitycms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
        Student student = studentRepository.findStudentByLogin(username);
        Teacher teacher = teacherRepository.findTeacherByLogin(username);
        Admin admin = adminRepository.findAdminByLogin(username);
        if(student != null) {
            return User.builder()
                    .username(student.getLogin())
                    .password(student.getPassword())
                    .roles("STUDENT")
                    .build();
        } else if (teacher != null) {
            return User.builder()
                    .username(teacher.getLogin())
                    .password(teacher.getPassword())
                    .roles("TEACHER")
                    .build();
        } else if(admin != null) {
            return User.builder()
                    .username(admin.getLogin())
                    .password(admin.getPassword())
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
