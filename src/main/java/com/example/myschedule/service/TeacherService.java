package com.example.myschedule.service;

import com.example.myschedule.dto.TeacherDTO;
import com.example.myschedule.entity.Role;
import com.example.myschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;

    public List<TeacherDTO> getAllTeachers() {
        log.info("Fetching all teachers from the database...");
        List<TeacherDTO> teachers = userRepository.findAll()
                .stream()
                .filter(user -> {
                    boolean isTeacher = user.getRole().name().equals(Role.TEACHER.name());
                    if (!isTeacher) {
                        log.debug("Skipping user with ID {} as they are not a teacher.", user.getUserId());
                    }
                    return isTeacher;
                })
                .map(TeacherDTO::toTeacherDTO)
                .collect(Collectors.toList());
        log.info("Found {} teachers.", teachers.size());
        return teachers;
    }
}
