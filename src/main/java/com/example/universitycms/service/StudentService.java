package com.example.universitycms.service;

import com.example.universitycms.model.Role;
import com.example.universitycms.model.Student;
import com.example.universitycms.repository.RoleRepository;
import com.example.universitycms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService  {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void addStudent(Student student) {

        long studentId = 3;

        if(studentRepository.existsByLogin(student.getLogin())) {
            throw new IllegalArgumentException("This login exists!");
        }

        if(studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("This E-mail exists!");
        }

        Role role = roleRepository.findRoleByRoleId(studentId);
        student.setRole(role);

        studentRepository.save(student);
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    public Student getStudentByLogin(String login) {

        if(login == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!studentRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Student with this login doesn't exist!");
        }

        return studentRepository.findStudentByLogin(login);
    }


    public Student getStudentByEmail(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Student with this email doesn't exist!");
        }

        return studentRepository.findStudentByEmail(email);
    }

    public Student getStudentByStudentId(long studentId) {

        if(!studentRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("Student with this Id doesn't exist!");
        }

        return studentRepository.findStudentByStudentId(studentId);
    }

}
