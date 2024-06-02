package com.example.universitycms.repository;


import com.example.universitycms.model.Faculty;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void save_shouldInsertStudent_whenInputContainsStudent() {
        Student student = new Student("Test login" , "Test password" , "Test email");
        studentRepository.save(student);
        long studentCount = studentRepository.count();
        assertEquals(11, studentCount);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void save_shouldThrowException_whenInputContainsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.save(null));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findAll_shouldReturnCorrectStudentList() {
        List<Student> studentList = studentRepository.findAll();
        assertEquals(10, studentList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByLogin_shouldReturnStudentWithCorrectLogin_whenInputContainsStudentWithExistingLogin() {
        String login = "student_john_doe";
        Student student = studentRepository.findStudentByLogin(login);
        assertEquals(student.getLogin(), login);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByLogin_shouldReturnNull_whenInputContainsStudentWithNotExistingLogin() {
        String login = "Test login";
        Student student = studentRepository.findStudentByLogin(login);
        assertNull(student);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByLogin_shouldReturnNull_whenInputContainsNull() {
        Student student = studentRepository.findStudentByLogin(null);
        assertNull(student);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByEmail_shouldReturnStudentWithCorrectEmail_whenInputContainsStudentWithExistingEmail() {
        String email = "john_doe@student.com";
        Student student = studentRepository.findStudentByEmail(email);
        assertEquals(student.getEmail(), email);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByEmail_shouldReturnNull_whenInputContainsStudentWithNotExistingEmail() {
        String email = "Test email";
        Student student = studentRepository.findStudentByEmail(email);
        assertNull(student);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByEmail_shouldReturnNull_whenInputContainsNull() {
        Student student = studentRepository.findStudentByEmail(null);
        assertNull(student);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByStudentId_shouldReturnStudentWithCorrectStudentId_whenInputContainsStudentWithExistingStudentId() {
        long studentId = 1;
        Student student = studentRepository.findStudentByStudentId(studentId);
        assertEquals(student.getStudentId(), studentId);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void findStudentByStudentId_shouldReturnNull_whenInputContainsStudentWithNotExistingStudentId() {
        long studentId = 100;
        Student student = studentRepository.findStudentByStudentId(studentId);
        assertNull(student);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByLogin_shouldReturnTrue_whenInputContainsStudentWithExistingLogin() {
        String login = "student_john_doe";
        boolean isLoginExists = studentRepository.existsByLogin(login);
        assertTrue(isLoginExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByLogin_shouldReturnFalse_whenInputContainsStudentWithNotExistingLogin() {
        String login = "Test login";
        boolean isLoginExists = studentRepository.existsByLogin(login);
        assertFalse(isLoginExists);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByLogin_shouldReturnFalse_whenInputContainsNull() {
        boolean isLoginExists = studentRepository.existsByLogin(null);
        assertFalse(isLoginExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByEmail_shouldReturnTrue_whenInputContainsStudentWithExistingEmail() {
        String email = "john_doe@student.com";
        boolean isEmailExists = studentRepository.existsByEmail(email);
        assertTrue(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsStudentWithNotExistingEmail() {
        String email = "Test email";
        boolean isEmailExists = studentRepository.existsByEmail(email);
        assertFalse(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsNull() {
        boolean isEmailExists = studentRepository.existsByEmail(null);
        assertFalse(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByStudentId_shouldReturnTrue_whenInputContainsStudentWithExistingStudentId() {
        long studentId = 1;
        boolean isStudentIdExists = studentRepository.existsByStudentId(studentId);
        assertTrue(isStudentIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_roles.sql", "/sql/insert_students.sql"})
    void existsByStudentId_shouldReturnFalse_whenInputContainsStudentWithNotExistingStudentId() {
        long studentId = 100;
        boolean isStudentIdExists = studentRepository.existsByStudentId(studentId);
        assertFalse(isStudentIdExists);
    }

}
