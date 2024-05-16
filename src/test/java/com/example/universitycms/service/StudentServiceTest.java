package com.example.universitycms.service;


import com.example.universitycms.model.Faculty;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Student;
import com.example.universitycms.model.Teacher;
import com.example.universitycms.repository.StudentRepository;
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

    @Test
    void save_shouldInsertStudentToDatabase_whenInputContainsStudent() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number", "Test first name", "Test last name", 50,  new Group("Test group"), new Faculty("Test faculty"));
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.existsByPhoneNumber(student.getPhoneNumber())).thenReturn(false);
        studentService.save(student);
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository).save(student);
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenInputContainsStudentWithAlreadyExistingLogin() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number", "Test first name", "Test last name", 50,  new Group("Test group"), new Faculty("Test faculty"));
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.save(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository,never()).existsByEmail(student.getEmail());
        verify(studentRepository,never()).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenInputContainsStudentWithAlreadyExistingEmail() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number", "Test first name", "Test last name", 50,  new Group("Test group"), new Faculty("Test faculty"));
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.save(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository,never()).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenInputContainsStudentWithAlreadyExistingPhoneNumber() {
        Student student = new Student("Test login" , "Test password" , "Test email", "Test phone number", "Test first name", "Test last name", 50,  new Group("Test group"), new Faculty("Test faculty"));
        when(studentRepository.existsByLogin(student.getLogin())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.existsByPhoneNumber(student.getPhoneNumber())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.save(student));
        verify(studentRepository).existsByLogin(student.getLogin());
        verify(studentRepository).existsByEmail(student.getEmail());
        verify(studentRepository).existsByPhoneNumber(student.getPhoneNumber());
        verify(studentRepository,never()).save(student);
    }


    @Test
    void findStudentByLogin_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingLogin() {
        List<Student> studentList = new LinkedList<>();
        studentList.add(new Student("Test login 1" , "Test password 1" , "Test email 1", "Test phone number 1", "Test first name 1", "Test last name 1", 50,  new Group("Test group 1"), new Faculty("Test faculty 1")));
        studentList.add(new Student("Test login 2" , "Test password 2" , "Test email 2", "Test phone number 2", "Test first name 2", "Test last name 2", 50,  new Group("Test group 2"), new Faculty("Test faculty 2")));
        studentList.add(new Student("Test login 3" , "Test password 3" , "Test email 3", "Test phone number 3", "Test first name 3", "Test last name 3", 50,  new Group("Test group 3"), new Faculty("Test faculty 3")));
        studentList.add(new Student("Test login 4" , "Test password 4" , "Test email 4", "Test phone number 4", "Test first name 4", "Test last name 4", 50,  new Group("Test group 4"), new Faculty("Test faculty 4")));
        studentList.add(new Student("Test login 5" , "Test password 5" , "Test email 5", "Test phone number 5", "Test first name 5", "Test last name 5", 50,  new Group("Test group 5"), new Faculty("Test faculty 5")));
        String login = "Test login 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByLogin(login)).thenReturn(true);
        when(studentRepository.findStudentByLogin(login)).thenReturn(expectedStudent);
        Student actualStudent = studentService.findStudentByLogin(login);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByLogin(login);
        verify(studentRepository).findStudentByLogin(login);
    }

    @Test
    void findStudentByLogin_shouldThrowIllegalArgumentException_whenInputContainsStudentWithNotExistingLogin() {
        String login = "Test login";
        when(studentRepository.existsByLogin(login)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByLogin(login));
        assertEquals("Student with this login doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByLogin(login);
        verify(studentRepository,never()).findStudentByLogin(login);
    }

    @Test
    void findStudentByLogin_shouldThrowIllegalArgumentException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByLogin(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByLogin(null);
        verify(studentRepository,never()).findStudentByLogin(null);
    }

    @Test
    void findStudentByPhoneNumber_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingPhoneNumber() {
        List<Student> studentList = new LinkedList<>();
        studentList.add(new Student("Test login 1" , "Test password 1" , "Test email 1", "Test phone number 1", "Test first name 1", "Test last name 1", 50,  new Group("Test group 1"), new Faculty("Test faculty 1")));
        studentList.add(new Student("Test login 2" , "Test password 2" , "Test email 2", "Test phone number 2", "Test first name 2", "Test last name 2", 50,  new Group("Test group 2"), new Faculty("Test faculty 2")));
        studentList.add(new Student("Test login 3" , "Test password 3" , "Test email 3", "Test phone number 3", "Test first name 3", "Test last name 3", 50,  new Group("Test group 3"), new Faculty("Test faculty 3")));
        studentList.add(new Student("Test login 4" , "Test password 4" , "Test email 4", "Test phone number 4", "Test first name 4", "Test last name 4", 50,  new Group("Test group 4"), new Faculty("Test faculty 4")));
        studentList.add(new Student("Test login 5" , "Test password 5" , "Test email 5", "Test phone number 5", "Test first name 5", "Test last name 5", 50,  new Group("Test group 5"), new Faculty("Test faculty 5")));
        String phoneNumber = "Test phone number 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        when(studentRepository.findStudentByPhoneNumber(phoneNumber)).thenReturn(expectedStudent);
        Student actualStudent = studentService.findStudentByPhoneNumber(phoneNumber);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByPhoneNumber(phoneNumber);
        verify(studentRepository).findStudentByPhoneNumber(phoneNumber);
    }

    @Test
    void findStudentByPhoneNumber_shouldThrowIllegalArgumentException_whenInputContainsStudentWithNotExistingPhoneNumber() {
        String phoneNumber = "Test phone number";
        when(studentRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByPhoneNumber(phoneNumber));
        assertEquals("Student with this phone number doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByPhoneNumber(phoneNumber);
        verify(studentRepository,never()).findStudentByPhoneNumber(phoneNumber);
    }

    @Test
    void findStudentByPhoneNumber_shouldThrowIllegalArgumentException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByPhoneNumber(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByPhoneNumber(null);
        verify(studentRepository,never()).findStudentByPhoneNumber(null);
    }

    @Test
    void findStudentByEmail_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingEmail() {
        List<Student> studentList = new LinkedList<>();
        studentList.add(new Student("Test login 1" , "Test password 1" , "Test email 1", "Test phone number 1", "Test first name 1", "Test last name 1", 50,  new Group("Test group 1"), new Faculty("Test faculty 1")));
        studentList.add(new Student("Test login 2" , "Test password 2" , "Test email 2", "Test phone number 2", "Test first name 2", "Test last name 2", 50,  new Group("Test group 2"), new Faculty("Test faculty 2")));
        studentList.add(new Student("Test login 3" , "Test password 3" , "Test email 3", "Test phone number 3", "Test first name 3", "Test last name 3", 50,  new Group("Test group 3"), new Faculty("Test faculty 3")));
        studentList.add(new Student("Test login 4" , "Test password 4" , "Test email 4", "Test phone number 4", "Test first name 4", "Test last name 4", 50,  new Group("Test group 4"), new Faculty("Test faculty 4")));
        studentList.add(new Student("Test login 5" , "Test password 5" , "Test email 5", "Test phone number 5", "Test first name 5", "Test last name 5", 50,  new Group("Test group 5"), new Faculty("Test faculty 5")));
        String email = "Test email 1";
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByEmail(email)).thenReturn(true);
        when(studentRepository.findStudentByEmail(email)).thenReturn(expectedStudent);
        Student actualStudent = studentService.findStudentByEmail(email);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByEmail(email);
        verify(studentRepository).findStudentByEmail(email);
    }

    @Test
    void findStudentByEmail_shouldThrowIllegalArgumentException_whenInputContainsStudentWithNotExistingEmail() {
        String email = "Test email";
        when(studentRepository.existsByEmail(email)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByEmail(email));
        assertEquals("Student with this email doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByEmail(email);
        verify(studentRepository,never()).findStudentByEmail(email);
    }

    @Test
    void findStudentByEmail_shouldThrowIllegalArgumentException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByEmail(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(studentRepository,never()).existsByEmail(null);
        verify(studentRepository,never()).findStudentByEmail(null);
    }


    @Test
    void findStudentByStudentId_shouldReturnCorrectStudent_whenInputContainsStudentWithExistingStudentId() {
        List<Student> studentList = LongStream.range(0, 10)
                .mapToObj(teacherId -> {
                    Student student = new Student();
                    student.setStudentId(teacherId);
                    return student;
                })
                .collect(Collectors.toList());
        long studentId = 1;
        Student expectedStudent = studentList.get(0);
        when(studentRepository.existsByStudentId(studentId)).thenReturn(true);
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(expectedStudent);
        Student actualStudent = studentService.findStudentByStudentId(studentId);
        assertEquals(expectedStudent, actualStudent);
        verify(studentRepository).existsByStudentId(studentId);
        verify(studentRepository).findStudentByStudentId(studentId);
    }

    @Test
    void findStudentByStudentId_shouldThrowIllegalArgumentException_whenInputContainsStudentWithNotExistingStudentId() {
        long studentId = 100;
        when(studentRepository.existsByStudentId(studentId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> studentService.findStudentByStudentId(studentId));
        assertEquals("Student with this Id doesn't exist!", exception.getMessage());
        verify(studentRepository).existsByStudentId(studentId);
        verify(studentRepository,never()).findStudentByStudentId(studentId);
    }

}