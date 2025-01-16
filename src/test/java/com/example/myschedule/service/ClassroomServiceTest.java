package com.example.myschedule.service;


import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.entity.Classroom;
import com.example.myschedule.exception.ClassroomException;
import com.example.myschedule.exception.ClassroomNotFoundException;
import com.example.myschedule.repository.ClassroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @InjectMocks
    ClassroomService classroomService;
    @Mock
    ClassroomRepository classroomRepository;
    Classroom classroom;
    ClassroomDTO classroomDTO;
    @BeforeEach
    void setUp(){

        long number = 400L;
        String description = "description";

        classroom = Classroom.builder()
                .number(number)
                .description(description)
                .build();

        classroomDTO = ClassroomDTO.builder()
                .number(number)
                .description(description)
                .build();
    }


    @Test
    void createClassroom_shouldReturnClassroomDTO() {

        when(classroomRepository.existsByNumber(classroomDTO.getNumber()))
                .thenReturn(false);

        when(classroomRepository.save(any(Classroom.class)))
                .thenReturn(classroom);

        ClassroomDTO actualClassroomDTO = classroomService.createClassroom(classroomDTO);

        assertNotNull(actualClassroomDTO);
        assertEquals(classroomDTO, actualClassroomDTO);

        verify(classroomRepository).existsByNumber(classroomDTO.getNumber());
        verify(classroomRepository).save(any(Classroom.class));

    }


    @Test
    void createClassroom_shouldThrowException_whenNumberRepeats() {

        when(classroomRepository.existsByNumber(classroomDTO.getNumber()))
                .thenReturn(true);

        ClassroomException exception = assertThrows(ClassroomException.class, () -> classroomService.createClassroom(classroomDTO));

        assertEquals("Classroom with " + classroomDTO.getNumber() + " number already exists!", exception.getMessage());

        verify(classroomRepository).existsByNumber(classroomDTO.getNumber());
        verify(classroomRepository, never()).save(any(Classroom.class));

    }

    @Test
    void updateClassroom_shouldReturnClassroomDTO() {

        classroomDTO.setNumber(500L);
        classroomDTO.setDescription("Description 2");

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.of(classroom));

        when(classroomRepository.existsByNumber(classroomDTO.getNumber()))
                .thenReturn(false);

        ClassroomDTO actualClassroomDTO = classroomService.updateClassroom(classroomDTO);

        assertNotNull(actualClassroomDTO);
        assertEquals(classroomDTO, actualClassroomDTO);

        verify(classroomRepository).findById(classroomDTO.getClassRoomId());
        verify(classroomRepository).existsByNumber(classroomDTO.getNumber());

    }

    @Test
    void updateClassroom_shouldThrowException_whenClassroomNotFound() {

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.empty());

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> classroomService.updateClassroom(classroomDTO));

        assertEquals("Classroom with " + classroomDTO.getClassRoomId() + " Id not found!", exception.getMessage());

        verify(classroomRepository).findById(classroomDTO.getClassRoomId());
        verify(classroomRepository, never()).existsByNumber(classroomDTO.getNumber());

    }
    @Test
    void updateClassroom_shouldThrowException_whenNumberRepeats() {

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.of(classroom));

        when(classroomRepository.existsByNumber(classroomDTO.getNumber()))
                .thenReturn(true);

        ClassroomException exception = assertThrows(ClassroomException.class, () -> classroomService.updateClassroom(classroomDTO));

        assertEquals("Classroom with " + classroomDTO.getNumber() + " number already exists!", exception.getMessage());

        verify(classroomRepository).findById(classroomDTO.getClassRoomId());
        verify(classroomRepository).existsByNumber(classroomDTO.getNumber());

    }


    @Test
    void getAll_shouldReturnClassroomDTOList() {

        when(classroomRepository.findAll())
                .thenReturn(List.of(classroom));

        List<ClassroomDTO> classroomDTOList = classroomService.getAll();

        assertNotNull(classroomDTOList);
        assertFalse(classroomDTOList.isEmpty());
        assertEquals(1, classroomDTOList.size());

        verify(classroomRepository).findAll();

    }

    @Test
    void getById_shouldReturnClassroomDTO() {

        long classroomId = 1L;

        when(classroomRepository.findById(classroomId))
                .thenReturn(Optional.of(classroom));

        ClassroomDTO actualClassroomDTO = classroomService.getById(classroomId);

        assertNotNull(actualClassroomDTO);
        assertEquals(classroomDTO, actualClassroomDTO);


        verify(classroomRepository).findById(classroomId);

    }

    @Test
    void getById_shouldThrowException_whenClassroomNotFound() {

        long classroomId = 100L;

        when(classroomRepository.findById(classroomId))
                .thenReturn(Optional.empty());

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> classroomService.getById(classroomId));

        assertEquals("Classroom with " + classroomId + " Id not found!", exception.getMessage());

        verify(classroomRepository).findById(classroomId);

    }

    @Test
    void getByNumber_shouldReturnClassroomDTO() {

        long number = 500L;

        when(classroomRepository.findByNumber(number))
                .thenReturn(Optional.of(classroom));

        ClassroomDTO actualClassroomDTO = classroomService.getByNumber(number);

        assertNotNull(actualClassroomDTO);
        assertEquals(classroomDTO, actualClassroomDTO);


        verify(classroomRepository).findByNumber(number);

    }

    @Test
    void getByNumber_shouldThrowException_whenClassroomNotFound() {

        long number = 100L;

        when(classroomRepository.findByNumber(number))
                .thenReturn(Optional.empty());

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> classroomService.getByNumber(number));

        assertEquals("Classroom with " + number + " number not found!", exception.getMessage());

        verify(classroomRepository).findByNumber(number);

    }

    @Test
    void removeById_shouldDeleteClassroom() {

        long classroomId = 1L;

        when(classroomRepository.existsById(classroomId))
                .thenReturn(true);

        doNothing().when(classroomRepository).deleteById(classroomId);

        classroomService.removeById(classroomId);

        verify(classroomRepository).existsById(classroomId);
        verify(classroomRepository).deleteById(classroomId);

    }

    @Test
    void removeById_shouldThrowException_whenClassroomNotFound() {

        long classroomId = 100L;

        when(classroomRepository.existsById(classroomId))
                .thenReturn(false);

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> classroomService.removeById(classroomId));

        assertEquals("Classroom with " + classroomId + " Id not found!", exception.getMessage());

        verify(classroomRepository).existsById(classroomId);
        verify(classroomRepository, never()).deleteById(classroomId);

    }

}
