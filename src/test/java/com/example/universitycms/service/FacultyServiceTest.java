package com.example.universitycms.service;

import com.example.universitycms.model.Faculty;
import com.example.universitycms.repository.FacultyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FacultyServiceTest {
    @Autowired
    FacultyService facultyService;
    @MockBean
    FacultyRepository facultyRepository;
    static List<Faculty> facultyList = new LinkedList<>();
    @BeforeAll
    static void init() {
        facultyList.add(new Faculty("Faculty name 1"));
        facultyList.add(new Faculty("Faculty name 2"));
        facultyList.add(new Faculty("Faculty name 3"));
        facultyList.add(new Faculty("Faculty name 4"));
        facultyList.add(new Faculty("Faculty name 5"));
    }

    @Test
    void getAll_shouldReturnCorrectFacultyList(){
        when(facultyRepository.findAll()).thenReturn(facultyList);
        List<Faculty> actualFacultyList = facultyService.getAll();
        assertEquals(facultyList,actualFacultyList);
        verify(facultyRepository).findAll();
    }
    @Test
    void getFacultyByFacultyName_shouldReturnCorrectFaculty_whenInputContainsExistingFaculty() {
        String facultyName = "Faculty name 1";
        Faculty expectedFaculty = facultyList.get(0);
        when(facultyRepository.existsByFacultyName(facultyName)).thenReturn(true);
        when(facultyRepository.findFacultyByFacultyName(facultyName)).thenReturn(expectedFaculty);
        Faculty actualFaculty = facultyService.getFacultyByFacultyName(facultyName);
        assertEquals(expectedFaculty,actualFaculty);
        verify(facultyRepository).existsByFacultyName(facultyName);
        verify(facultyRepository).findFacultyByFacultyName(facultyName);
    }
    @Test
    void getFacultyByFacultyName_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> facultyService.getFacultyByFacultyName(null));
        assertEquals("Input contains null!",exception.getMessage());
        verify(facultyRepository, never()).existsByFacultyName(null);
        verify(facultyRepository,never()).findFacultyByFacultyName(null);
    }
    @Test
    void getFacultyByFacultyName_shouldThrowException_whenInputContainsNotExistingFaculty() {
        String facultyName = "Test faculty name";
        when(facultyRepository.existsByFacultyName(facultyName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> facultyService.getFacultyByFacultyName(facultyName));
        assertEquals("Faculty name doesn't exists!",exception.getMessage());
        verify(facultyRepository).existsByFacultyName(facultyName);
        verify(facultyRepository,never()).findFacultyByFacultyName(facultyName);
    }

    @Test
    void getFacultyByFacultyId_shouldReturnCorrectFaculty_whenInputContainsExistingFaculty() {
        List<Faculty> facultyList = LongStream.range(0, 10)
                .mapToObj(facultyId -> {
                    Faculty faculty = new Faculty();
                    faculty.setFacultyId(facultyId);
                    return faculty;
                })
                .collect(Collectors.toList());
        long facultyId = 1;
        Faculty expectedFaculty = facultyList.get(0);
        when(facultyRepository.existsByFacultyId(facultyId)).thenReturn(true);
        when(facultyRepository.findFacultyByFacultyId(facultyId)).thenReturn(expectedFaculty);
        Faculty actualFaculty = facultyService.getFacultyByFacultyId(facultyId);
        assertEquals(expectedFaculty,actualFaculty);
        verify(facultyRepository).existsByFacultyId(facultyId);
        verify(facultyRepository).findFacultyByFacultyId(facultyId);
    }
    @Test
    void getFacultyByFacultyId_shouldThrowException_whenInputContainsNotExistingFaculty() {
        long facultyId = 100;
        when(facultyRepository.existsByFacultyId(facultyId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> facultyService.getFacultyByFacultyId(facultyId));
        assertEquals("Faculty Id doesn't exists!",exception.getMessage());
        verify(facultyRepository).existsByFacultyId(facultyId);
        verify(facultyRepository,never()).findFacultyByFacultyId(facultyId);
    }

}