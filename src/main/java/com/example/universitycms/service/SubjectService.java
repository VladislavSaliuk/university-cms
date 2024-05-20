package com.example.universitycms.service;

import com.example.universitycms.model.Subject;
import com.example.universitycms.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectBySubjectName(String subjectName) {

        if(subjectName == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!subjectRepository.existsBySubjectName(subjectName)) {
            throw new IllegalArgumentException("Subject name doesn't exists!");
        }

        return subjectRepository.findSubjectBySubjectName(subjectName);
    }

    public Subject getSubjectBySubjectId(long subjectId) {

        if(!subjectRepository.existsBySubjectId(subjectId)) {
            throw new IllegalArgumentException("Subject Id doesn't exists!");
        }

        return subjectRepository.findSubjectBySubjectId(subjectId);
    }

}
