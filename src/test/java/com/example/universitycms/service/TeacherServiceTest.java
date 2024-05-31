package com.example.universitycms.service;

import com.example.universitycms.model.Teacher;
import com.example.universitycms.repository.TeacherRepository;
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
public class TeacherServiceTest {

    @Autowired
    TeacherService teacherService;

    @MockBean
    TeacherRepository teacherRepository;

    static List<Teacher> teacherList = new LinkedList<>();
    @BeforeAll
    static void init() {
        teacherList.add(new Teacher("Test login 1" , "Test password 1" , "Test email 1"));
        teacherList.add(new Teacher("Test login 2" , "Test password 2" , "Test email 2"));
        teacherList.add(new Teacher("Test login 3" , "Test password 3" , "Test email 3"));
        teacherList.add(new Teacher("Test login 4" , "Test password 4" , "Test email 4"));
        teacherList.add(new Teacher("Test login 5" , "Test password 5" , "Test email 5"));
    }

    @Test
    void addTeacher_shouldInsertTeacherToDatabase_whenInputContainsTeacher() {
        Teacher teacher = new Teacher("Test login" , "Test password" , "Test email");
        when(teacherRepository.existsByLogin(teacher.getLogin())).thenReturn(false);
        when(teacherRepository.existsByEmail(teacher.getEmail())).thenReturn(false);
        teacherService.addTeacher(teacher);
        verify(teacherRepository).existsByLogin(teacher.getLogin());
        verify(teacherRepository).existsByEmail(teacher.getEmail());
        verify(teacherRepository).save(teacher);
    }

    @Test
    void addTeacher_shouldThrowException_whenInputContainsTeacherWithAlreadyExistingLogin() {
        Teacher teacher = new Teacher("Test login" , "Test password" , "Test email");
        when(teacherRepository.existsByLogin(teacher.getLogin())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher(teacher));
        verify(teacherRepository).existsByLogin(teacher.getLogin());
        verify(teacherRepository,never()).existsByEmail(teacher.getEmail());
        verify(teacherRepository,never()).save(teacher);
    }

    @Test
    void addTeacher_shouldThrowException_whenInputContainsTeacherWithAlreadyExistingEmail() {
        Teacher teacher = new Teacher("Test login" , "Test password" , "Test email");
        when(teacherRepository.existsByLogin(teacher.getLogin())).thenReturn(false);
        when(teacherRepository.existsByEmail(teacher.getEmail())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher(teacher));
        verify(teacherRepository).existsByLogin(teacher.getLogin());
        verify(teacherRepository).existsByEmail(teacher.getEmail());
        verify(teacherRepository,never()).save(teacher);
    }

    @Test
    void addTeacher_shouldThrowException_whenInputContainsTeacherWithAlreadyExistingPhoneNumber() {
        Teacher teacher = new Teacher("Test login" , "Test password" , "Test email");
        when(teacherRepository.existsByLogin(teacher.getLogin())).thenReturn(false);
        when(teacherRepository.existsByEmail(teacher.getEmail())).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher(teacher));
        verify(teacherRepository).existsByLogin(teacher.getLogin());
        verify(teacherRepository).existsByEmail(teacher.getEmail());
        verify(teacherRepository,never()).save(teacher);
    }

    @Test
    void getAll_shouldReturnCorrectTeacherList() {
        when(teacherRepository.findAll()).thenReturn(teacherList);
        List<Teacher> expectedTeacherList = teacherService.getAll();
        assertEquals(teacherList, expectedTeacherList);
        verify(teacherRepository).findAll();
    }

    @Test
    void getTeacherByLogin_shouldReturnCorrectTeacher_whenInputContainsTeacherWithExistingLogin() {
        String login = "Test login 1";
        Teacher expectedTeacher = teacherList.get(0);
        when(teacherRepository.existsByLogin(login)).thenReturn(true);
        when(teacherRepository.findTeacherByLogin(login)).thenReturn(expectedTeacher);
        Teacher actualTeacher = teacherService.getTeacherByLogin(login);
        assertEquals(expectedTeacher, actualTeacher);
        verify(teacherRepository).existsByLogin(login);
        verify(teacherRepository).findTeacherByLogin(login);
    }

    @Test
    void getTeacherByLogin_shouldThrowException_whenInputContainsTeacherWithNotExistingLogin() {
        String login = "Test login";
        when(teacherRepository.existsByLogin(login)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByLogin(login));
        assertEquals("Teacher with this login doesn't exist!", exception.getMessage());
        verify(teacherRepository).existsByLogin(login);
        verify(teacherRepository,never()).findTeacherByLogin(login);
    }

    @Test
    void getTeacherByLogin_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByLogin(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(teacherRepository,never()).existsByLogin(null);
        verify(teacherRepository,never()).findTeacherByLogin(null);
    }

    @Test
    void getTeacherByEmail_shouldReturnCorrectTeacher_whenInputContainsTeacherWithExistingEmail() {
        String email = "Test email 1";
        Teacher expectedTeacher = teacherList.get(0);
        when(teacherRepository.existsByEmail(email)).thenReturn(true);
        when(teacherRepository.findTeacherByEmail(email)).thenReturn(expectedTeacher);
        Teacher actualTeacher = teacherService.getTeacherByEmail(email);
        assertEquals(expectedTeacher, actualTeacher);
        verify(teacherRepository).existsByEmail(email);
        verify(teacherRepository).findTeacherByEmail(email);
    }

    @Test
    void getTeacherByEmail_shouldThrowException_whenInputContainsTeacherWithNotExistingEmail() {
        String email = "Test email";
        when(teacherRepository.existsByEmail(email)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByEmail(email));
        assertEquals("Teacher with this email doesn't exist!", exception.getMessage());
        verify(teacherRepository).existsByEmail(email);
        verify(teacherRepository,never()).findTeacherByEmail(email);
    }

    @Test
    void getTeacherByEmail_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByEmail(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(teacherRepository,never()).existsByEmail(null);
        verify(teacherRepository,never()).findTeacherByEmail(null);
    }


    @Test
    void getTeacherByTeacherId_shouldReturnCorrectTeacher_whenInputContainsTeacherWithExistingTeacherId() {
        List<Teacher> teacherList = LongStream.range(0, 10)
                .mapToObj(teacherId -> {
                    Teacher teacher = new Teacher();
                    teacher.setTeacherId(teacherId);
                    return teacher;
                })
                .collect(Collectors.toList());
        long teacherId = 1;
        Teacher expectedTeacher = teacherList.get(0);
        when(teacherRepository.existsByTeacherId(teacherId)).thenReturn(true);
        when(teacherRepository.findTeacherByTeacherId(teacherId)).thenReturn(expectedTeacher);
        Teacher actualTeacher = teacherService.getTeacherByTeacherId(teacherId);
        assertEquals(expectedTeacher, actualTeacher);
        verify(teacherRepository).existsByTeacherId(teacherId);
        verify(teacherRepository).findTeacherByTeacherId(teacherId);
    }

    @Test
    void getTeacherByTeacherId_shouldThrowException_whenInputContainsTeacherWithNotExistingTeacherId() {
        long teacherId = 100;
        when(teacherRepository.existsByTeacherId(teacherId)).thenReturn(false)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByTeacherId(teacherId));
        assertEquals("Teacher with this Id doesn't exist!", exception.getMessage());
        verify(teacherRepository).existsByTeacherId(teacherId);
        verify(teacherRepository,never()).findTeacherByTeacherId(teacherId);
    }

}
