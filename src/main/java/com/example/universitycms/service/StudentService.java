package com.example.universitycms.service;

import com.example.universitycms.model.Student;
import com.example.universitycms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void save(Student student) {

        if(studentRepository.existsByLogin(student.getLogin())) {
            throw new IllegalArgumentException("This login exists!");
        }

        if(studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("This E-mail exists!");
        }

        if(studentRepository.existsByPhoneNumber(student.getPhoneNumber())) {
            throw new IllegalArgumentException("This phone number exists");
        }

        studentRepository.save(student);
    }

    public Student findStudentByLogin(String login) {

        if(login == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!studentRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Student with this login doesn't exist!");
        }

        return studentRepository.findStudentByLogin(login);
    }

    public Student findStudentByPhoneNumber(String phoneNumber) {

        if(phoneNumber == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!studentRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Student with this phone number doesn't exist!");
        }

        return studentRepository.findStudentByPhoneNumber(phoneNumber);
    }

    public Student findStudentByEmail(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Student with this email doesn't exist!");
        }

        return studentRepository.findStudentByEmail(email);
    }

    public Student findStudentByStudentId(long studentId) {

        if(!studentRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("Student with this Id doesn't exist!");
        }

        return studentRepository.findStudentByStudentId(studentId);
    }

}
