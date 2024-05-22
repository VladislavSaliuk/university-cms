package com.example.universitycms.service;


import com.example.universitycms.model.Student;
import com.example.universitycms.repository.StudentRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
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
public class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @MockBean
    StudentRepository studentRepository;

    static List<Student> studentList = new LinkedList<>();

    @BeforeAll
    static void init() {
        studentList.add(new Student("Test login 1" , "Test password 1" , "Test email 1", "Test phone number 1"));
        studentList.add(new Student("Test login 2" , "Test password 2" , "Test email 2", "Test phone number 2"));
        studentList.add(new Student("Test login 3" , "Test password 3" , "Test email 3", "Test phone number 3"));
        studentList.add(new Student("Test login 4" , "Test password 4" , "Test email 4", "Test phone number 4"));
        studentList.add(new Student("Test login 5" , "Test password 5" , "Test email 5", "Test phone number 5"));
    }

    @Test
    void addStudent_shouldInsertStudentToDatabase_whenInputContainsStudent() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number");
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.existsByPhoneNumber(student.getPhoneNumber())).thenReturn(false);
        studentService.addStudent(student);
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository).save(student);
    }

    @Test
    void addStudent_shouldThrowException_whenInputContainsStudentWithAlreadyExistingLogin() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number");
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository,never()).existsByEmail(student.getEmail());
        verify(studentRepository,never()).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }

    @Test
    void addStudent_shouldThrowException_whenInputContainsStudentWithAlreadyExistingEmail() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number");
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository,never()).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }

    @Test
    void addStudent_shouldThrowException_whenInputContainsStudentWithAlreadyExistingPhoneNumber() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number");
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.existsByPhoneNumber(student.getPhoneNumber())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }

    @Test
    void gelAll_shouldReturnCorrectStudentList() {
        when(studentRepository.findAll()).thenReturn(studentList);
        List<Student> expectedStudentList = studentService.getAll();
        assertEquals(studentList, expectedStudentList);
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentByLogin_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingLogin() {
        String login = "Test login 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByLogin(login)).thenReturn(true);
        when(studentRepository.findStudentByLogin(login)).thenReturn(expectedStudent);
        Student actualStudent = studentService.getStudentByLogin(login);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByLogin(login);
        verify(studentRepository).findStudentByLogin(login);
    }

    @Test
    void findStudentByLogin_shouldThrowIllegalArgumentException_whenInputContainsStudentWithNotExistingLogin() {
        String login = "Test login";
        when(studentRepository.existsByLogin(login)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByLogin(login));
        assertEquals("Student with this login doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByLogin(login);
        verify(studentRepository,never()).findStudentByLogin(login);
    }

    @Test
    void getStudentByLogin_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByLogin(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByLogin(null);
        verify(studentRepository,never()).findStudentByLogin(null);
    }

    @Test
    void getStudentByPhoneNumber_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingPhoneNumber() {
        String phoneNumber = "Test phone number 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        when(studentRepository.findStudentByPhoneNumber(phoneNumber)).thenReturn(expectedStudent);
        Student actualStudent = studentService.getStudentByPhoneNumber(phoneNumber);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByPhoneNumber(phoneNumber);
        verify(studentRepository).findStudentByPhoneNumber(phoneNumber);
    }

    @Test
    void getStudentByPhoneNumber_shouldThrowException_whenInputContainsStudentWithNotExistingPhoneNumber() {
        String phoneNumber = "Test phone number";
        when(studentRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByPhoneNumber(phoneNumber));
        assertEquals("Student with this phone number doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByPhoneNumber(phoneNumber);
        verify(studentRepository,never()).findStudentByPhoneNumber(phoneNumber);
    }

    @Test
    void getStudentByPhoneNumber_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByPhoneNumber(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByPhoneNumber(null);
        verify(studentRepository,never()).findStudentByPhoneNumber(null);
    }

    @Test
    void getStudentByEmail_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingEmail() {
        String email = "Test email 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByEmail(email)).thenReturn(true);
        when(studentRepository.findStudentByEmail(email)).thenReturn(expectedStudent);
        Student actualStudent = studentService.getStudentByEmail(email);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByEmail(email);
        verify(studentRepository).findStudentByEmail(email);
    }

    @Test
    void getStudentByEmail_shouldThrowException_whenInputContainsStudentWithNotExistingEmail() {
        String email = "Test email";
        when(studentRepository.existsByEmail(email)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByEmail(email));
        assertEquals("Student with this email doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByEmail(email);
        verify(studentRepository,never()).findStudentByEmail(email);
    }

    @Test
    void getStudentByEmail_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByEmail(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByEmail(null);
        verify(studentRepository,never()).findStudentByEmail(null);
    }


    @Test
    void getStudentByStudentId_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingStudentId() {
        List<Student> studentList = LongStream.range(0, 10)
                .mapToObj(studentId -> {
                    Student student = new Student();
                    student.setStudentId(studentId);
                    return student;
                })
                .collect(Collectors.toList());
        long studentId = 1;
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByStudentId(studentId)).thenReturn(true);
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(expectedStudent);
        Student actualStudent = studentService.getStudentByStudentId(studentId);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByStudentId(studentId);
        verify(studentRepository).findStudentByStudentId(studentId);
    }

    @Test
    void getStudentByStudentId_shouldThrowException_whenInputContainsStudentWithNotExistingStudentId() {
        long studentId = 100;
        when(studentRepository.existsByStudentId(studentId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByStudentId(studentId));
        assertEquals("Student with this Id doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByStudentId(studentId);
        verify(studentRepository,never()).findStudentByStudentId(studentId);
    }

}