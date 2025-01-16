package com.example.myschedule.service;

import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.entity.Classroom;
import com.example.myschedule.exception.ClassroomException;
import com.example.myschedule.exception.ClassroomNotFoundException;
import com.example.myschedule.repository.ClassroomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomDTO createClassroom(ClassroomDTO classroomDTO) {
        log.info("Attempting to create a classroom with number: {}", classroomDTO.getNumber());

        if (classroomRepository.existsByNumber(classroomDTO.getNumber())) {
            log.warn("Classroom with number {} already exists!", classroomDTO.getNumber());
            throw new ClassroomException("Classroom with " + classroomDTO.getNumber() + " number already exists!");
        }

        Classroom classroom = classroomRepository.save(Classroom.toClassroom(classroomDTO));
        log.info("Successfully created classroom with ID: {} and number: {}", classroom.getClassroomId(), classroom.getNumber());

        return ClassroomDTO.toClassroomDTO(classroom);
    }

    @Transactional
    public ClassroomDTO updateClassroom(ClassroomDTO classroomDTO) {
        log.info("Attempting to update classroom with ID: {}", classroomDTO.getClassRoomId());

        Classroom updatedClassroom = classroomRepository.findById(classroomDTO.getClassRoomId())
                .orElseThrow(() -> {
                    log.error("Classroom with ID {} not found!", classroomDTO.getClassRoomId());
                    return new ClassroomNotFoundException("Classroom with " + classroomDTO.getClassRoomId() + " Id not found!");
                });

        if (classroomRepository.existsByNumber(classroomDTO.getNumber())) {
            log.warn("Classroom with number {} already exists!", classroomDTO.getNumber());
            throw new ClassroomException("Classroom with " + classroomDTO.getNumber() + " number already exists!");
        }

        updatedClassroom.setNumber(classroomDTO.getNumber());
        updatedClassroom.setDescription(classroomDTO.getDescription());

        log.info("Successfully updated classroom with ID: {} to number: {}", updatedClassroom.getClassroomId(), updatedClassroom.getNumber());

        return ClassroomDTO.toClassroomDTO(updatedClassroom);
    }

    public List<ClassroomDTO> getAll() {
        log.info("Fetching all classrooms");

        List<ClassroomDTO> classrooms = classroomRepository.findAll()
                .stream()
                .map(ClassroomDTO::toClassroomDTO)
                .collect(Collectors.toList());

        log.info("Fetched {} classrooms", classrooms.size());
        return classrooms;
    }

    public ClassroomDTO getById(long classRoomId) {
        log.info("Fetching classroom with ID: {}", classRoomId);

        return classroomRepository.findById(classRoomId)
                .map(classroom -> {
                    log.info("Classroom found with ID: {}", classRoomId);
                    return ClassroomDTO.toClassroomDTO(classroom);
                })
                .orElseThrow(() -> {
                    log.error("Classroom with ID {} not found!", classRoomId);
                    return new ClassroomNotFoundException("Classroom with " + classRoomId + " Id not found!");
                });
    }

    public ClassroomDTO getByNumber(long number) {
        log.info("Fetching classroom with number: {}", number);

        return classroomRepository.findByNumber(number)
                .map(classroom -> {
                    log.info("Classroom found with number: {}", number);
                    return ClassroomDTO.toClassroomDTO(classroom);
                })
                .orElseThrow(() -> {
                    log.error("Classroom with number {} not found!", number);
                    return new ClassroomNotFoundException("Classroom with " + number + " number not found!");
                });
    }

    public void removeById(long classRoomId) {
        log.info("Attempting to remove classroom with ID: {}", classRoomId);

        if (!classroomRepository.existsById(classRoomId)) {
            log.warn("Classroom with ID {} not found!", classRoomId);
            throw new ClassroomNotFoundException("Classroom with " + classRoomId + " Id not found!");
        }

        classroomRepository.deleteById(classRoomId);
        log.info("Successfully removed classroom with ID: {}", classRoomId);
    }
}