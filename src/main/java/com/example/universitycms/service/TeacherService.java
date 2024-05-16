package com.example.universitycms.service;

import com.example.universitycms.model.Teacher;
import com.example.universitycms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public void save(Teacher teacher) {

        if(teacherRepository.existsByLogin(teacher.getLogin())) {
            throw new IllegalArgumentException("This login exists!");
        }

        if(teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("This E-mail exists!");
        }

        if(teacherRepository.existsByPhoneNumber(teacher.getPhoneNumber())) {
            throw new IllegalArgumentException("This phone number exists");
        }

        teacherRepository.save(teacher);
    }

    public Teacher findTeacherByLogin(String login) {

        if(login == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!teacherRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Teacher with this login doesn't exist!");
        }

        return teacherRepository.findTeacherByLogin(login);
    }

    public Teacher findTeacherByPhoneNumber(String phoneNumber) {

        if(phoneNumber == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!teacherRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Teacher with this phone number doesn't exist!");
        }

        return teacherRepository.findTeacherByPhoneNumber(phoneNumber);
    }

    public Teacher findTeacherByEmail(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!teacherRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Teacher with this email doesn't exist!");
        }

        return teacherRepository.findTeacherByEmail(email);
    }

    public Teacher findTeacherByTeacherId(long teacherId) {

        if(!teacherRepository.existsByTeacherId(teacherId)) {
            throw new IllegalArgumentException("Teacher with this Id doesn't exist!");
        }

        return teacherRepository.findTeacherByTeacherId(teacherId);
    }

}
