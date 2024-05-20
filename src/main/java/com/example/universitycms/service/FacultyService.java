package com.example.universitycms.service;

import com.example.universitycms.model.Faculty;
import com.example.universitycms.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyByFacultyName(String facultyName) {

        if(facultyName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!facultyRepository.existsByFacultyName(facultyName)) {
            throw new IllegalArgumentException("Faculty name doesn't exists!");
        }

        return facultyRepository.findFacultyByFacultyName(facultyName);
    }
    public Faculty getFacultyByFacultyId(long facultyId) {

        if(!facultyRepository.existsByFacultyId(facultyId)) {
            throw new IllegalArgumentException("Faculty Id doesn't exists!");
        }

        return facultyRepository.findFacultyByFacultyId(facultyId);
    }

}