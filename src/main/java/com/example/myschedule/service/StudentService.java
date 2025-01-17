package com.example.myschedule.service;

import com.example.myschedule.dto.StudentDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.User;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.exception.UserNotFoundException;
import com.example.myschedule.repository.GroupRepository;
import com.example.myschedule.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students from the database...");
        List<StudentDTO> students = userRepository.findAll()
                .stream()
                .filter(user -> {
                    boolean isStudent = user.getRole().name().equals(Role.STUDENT.name());
                    if (!isStudent) {
                        log.debug("Skipping user with ID {} as they are not a student.", user.getUserId());
                    }
                    return isStudent;
                })
                .map(StudentDTO::toStudentDTO)
                .collect(Collectors.toList());
        log.info("Found {} students.", students.size());
        return students;
    }

    @Transactional
    public StudentDTO assignStudentToGroup(StudentDTO studentDTO) {
        log.info("Assigning student with ID {} to group with ID {}.",
                studentDTO.getUserId(), studentDTO.getGroupDTO().getGroupId());

        User user = userRepository.findById(studentDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("Student with ID {} not found!", studentDTO.getUserId());
                    return new UserNotFoundException("Student with " + studentDTO.getUserId() + " Id not found!");
                });

        Group group = groupRepository.findById(studentDTO.getGroupDTO().getGroupId())
                .orElseThrow(() -> {
                    log.error("Group with ID {} not found!", studentDTO.getGroupDTO().getGroupId());
                    return new GroupNotFoundException("Group with " + studentDTO.getGroupDTO().getGroupId() + " Id not found!");
                });

        if (!user.getRole().name().equals(Role.STUDENT.name())) {
            log.error("User with ID {} is not a student!", studentDTO.getUserId());
            throw new UserException("User with " + studentDTO.getUserId() + " Id is not a student!");
        }

        log.debug("Assigning user {} to group {}.", user.getUsername(), group.getGroupName());
        user.setGroup(group);

        StudentDTO result = StudentDTO.toStudentDTO(user);
        log.info("Successfully assigned student {} to group {}.", result.getUsername(), group.getGroupName());
        return result;
    }

}