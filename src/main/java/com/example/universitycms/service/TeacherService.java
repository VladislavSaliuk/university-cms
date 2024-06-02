package com.example.universitycms.service;

import com.example.universitycms.model.Teacher;
import com.example.universitycms.repository.TeacherRepository;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public void addTeacher(Teacher teacher) {

        if(teacherRepository.existsByLogin(teacher.getLogin())) {
            throw new IllegalArgumentException("This login exists!");
        }

        if(teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("This E-mail exists!");
        }


        teacherRepository.save(teacher);
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherByLogin(String login) {

        if(login == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!teacherRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Teacher with this login doesn't exist!");
        }

        return teacherRepository.findTeacherByLogin(login);
    }

    public Teacher getTeacherByEmail(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!teacherRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Teacher with this email doesn't exist!");
        }

        return teacherRepository.findTeacherByEmail(email);
    }

    public Teacher getTeacherByTeacherId(long teacherId) {

        if(!teacherRepository.existsByTeacherId(teacherId)) {
            throw new IllegalArgumentException("Teacher with this Id doesn't exist!");
        }

        return teacherRepository.findTeacherByTeacherId(teacherId);
    }


}
