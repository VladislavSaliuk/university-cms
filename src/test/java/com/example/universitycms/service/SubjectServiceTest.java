package com.example.universitycms.service;

import com.example.universitycms.model.Subject;
import com.example.universitycms.repository.SubjectRepository;
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

    @Test
    void findAll_shouldReturnCorrectSubjectList() {
        List<Subject> expectedSubjectList = new LinkedList<>();
        expectedSubjectList.add(new Subject("Test subject name 1", "description", LocalTime.now()));
        expectedSubjectList.add(new Subject("Test subject name 2", "description", LocalTime.now()));
        expectedSubjectList.add(new Subject("Test subject name 3", "description", LocalTime.now()));
        expectedSubjectList.add(new Subject("Test subject name 4", "description", LocalTime.now()));
        expectedSubjectList.add(new Subject("Test subject name 5", "description", LocalTime.now()));
        when(subjectRepository.findAll()).thenReturn(expectedSubjectList);
        List<Subject> actualSubjectList = subjectService.findAll();
        assertEquals(expectedSubjectList,actualSubjectList);
        verify(subjectRepository).findAll();
    }

    @Test
    void findSubjectBySubjectName_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingName() {
        List<Subject> subjectList = new LinkedList<>();
        subjectList.add(new Subject("Test subject name 1", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 2", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 3", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 4", "description", LocalTime.now()));
        subjectList.add(new Subject("Test subject name 5", "description", LocalTime.now()));
        String subjectName = "Test subject name 1";
        Subject expectedSubject = subjectList.get(0);
        when(subjectRepository.existsBySubjectName(subjectName)).thenReturn(true);
        when(subjectRepository.findSubjectBySubjectName(subjectName)).thenReturn(expectedSubject);
        Subject actualSubject = subjectService.findSubjectBySubjectName(subjectName);
        assertEquals(expectedSubject,actualSubject);
        verify(subjectRepository).existsBySubjectName(subjectName);
        verify(subjectRepository).findSubjectBySubjectName(subjectName);
    }

    @Test
    void findSubjectBySubjectName_shouldReturnIllegalArgumentException_whenInputContainsSubjectWithNotExistingName() {
        String subjectName = "Test subject name ";
        when(subjectRepository.existsBySubjectName(subjectName)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.findSubjectBySubjectName(subjectName));
        assertEquals("Subject name doesn't exists!",exception.getMessage());
        verify(subjectRepository).existsBySubjectName(subjectName);
        verify(subjectRepository,never()).findSubjectBySubjectName(subjectName);
    }

    @Test
    void findSubjectBySubjectName_shouldReturnIllegalArgumentException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.findSubjectBySubjectName(null));
        assertEquals("Input contains null!",exception.getMessage());
        verify(subjectRepository,never()).existsBySubjectName(null);
        verify(subjectRepository,never()).findSubjectBySubjectName(null);
    }

    @Test
    void findSubjectBySubjectId_shouldReturnCorrectSubject_whenInputContainsSubjectWithExistingSubjectName() {
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
        Subject actualSubject = subjectService.findSubjectBySubjectId(subjectId);
        assertEquals(expectedSubject,actualSubject);
        verify(subjectRepository).existsBySubjectId(subjectId);
        verify(subjectRepository).findSubjectBySubjectId(subjectId);
    }

    @Test
    void findSubjectBySubjectId_shouldReturnIllegalArgumentException_whenInputContainsSubjectWithNotExistingSubjectId() {
        long subjectId = 100;
        when(subjectRepository.existsBySubjectId(subjectId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> subjectService.findSubjectBySubjectId(subjectId));
        assertEquals("Subject Id doesn't exists!",exception.getMessage());
        verify(subjectRepository).existsBySubjectId(subjectId);
        verify(subjectRepository,never()).findSubjectBySubjectId(subjectId);
    }


}
