package com.example.universitycms.service;

import com.example.universitycms.model.Subject;
import com.example.universitycms.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubjectServiceTest {

    @Autowired
    SubjectService subjectService;

    @MockBean
    SubjectRepository subjectRepository;

    static List<Subject> subjectList = new LinkedList<>();

    @BeforeAll
    static void init() {
        subjectList.add(new Subject("Test subject name 1", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 2", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 3", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 4", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 5", "description", LocalTime.now()));
    }

    @Test
    void getAll_shouldReturnCorrectSubjectList() {
        when(subjectRepository.findAll()).thenReturn(subjectList);
        List<Subject> actualSubjectList = subjectService.getAll();
        assertEquals(subjectList,actualSubjectList);
        verify(subjectRepository).findAll();
    }

    @Test
    void getSubjectBySubjectName_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingName() {
        String subjectName = "Test subject name 1";
        Subject expectedSubject = subjectList.get(0);
        when(subjectRepository.existsBySubjectName(subjectName)).thenReturn(true);
        when(subjectRepository.findSubjectBySubjectName(subjectName)).thenReturn(expectedSubject);
        Subject actualSubject = subjectService.getSubjectBySubjectName(subjectName);
        assertEquals(expectedSubject,actualSubject);
        verify(subjectRepository).existsBySubjectName(subjectName);
        verify(subjectRepository).findSubjectBySubjectName(subjectName);
    }

    @Test
    void getSubjectBySubjectName_shouldReturnException_whenInputContainsSubjectWithNotExistingName() {
        String subjectName = "Test subject name ";
        when(subjectRepository.existsBySubjectName(subjectName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.getSubjectBySubjectName(subjectName));
        assertEquals("Subject name doesn't exists!",exception.getMessage());
        verify(subjectRepository).existsBySubjectName(subjectName);
        verify(subjectRepository,never()).findSubjectBySubjectName(subjectName);
    }

    @Test
    void getSubjectBySubjectName_shouldReturnException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.getSubjectBySubjectName(null));
        assertEquals("Input contains null!",exception.getMessage());
        verify(subjectRepository,never()).existsBySubjectName(null);
        verify(subjectRepository,never()).findSubjectBySubjectName(null);
    }

    @Test
    void getSubjectBySubjectId_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingSubjectName() {
        List<Subject> subjectList = LongStream.range(0, 10)
                .mapToObj(subjectId -> {
                    Subject subject = new Subject();
                    subject.setSubjectId(subjectId);
                    return subject;
                })
                .collect(Collectors.toList());
        long subjectId = 1;
        Subject expectedSubject = subjectList.get(0);
        when(subjectRepository.existsBySubjectId(subjectId)).thenReturn(true);
        when(subjectRepository.findSubjectBySubjectId(subjectId)).thenReturn(expectedSubject);
        Subject actualSubject = subjectService.getSubjectBySubjectId(subjectId);
        assertEquals(expectedSubject,actualSubject);
        verify(subjectRepository).existsBySubjectId(subjectId);
        verify(subjectRepository).findSubjectBySubjectId(subjectId);
    }

    @Test
    void getSubjectBySubjectId_shouldThrowException_whenInputContainsSubjectWithNotExistingSubjectId() {
        long subjectId = 100;
        when(subjectRepository.existsBySubjectId(subjectId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.getSubjectBySubjectId(subjectId));
        assertEquals("Subject Id doesn't exists!",exception.getMessage());
        verify(subjectRepository).existsBySubjectId(subjectId);
        verify(subjectRepository,never()).findSubjectBySubjectId(subjectId);
    }


}
