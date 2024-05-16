package com.example.universitycms.repository;

import com.example.universitycms.model.Faculty;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.Teacher;
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
public class TeacherRepositoryTest {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void save_shouldInsertTeacher_whenInputContainsTeacher() {
        Group group = groupRepository.findGroupByGroupId(1);
        Faculty faculty = facultyRepository.findFacultyByFacultyId(1);
        Teacher teacher = new Teacher("Test login" , "Test password" , "Test email", "Test phone number", "Test first name", "Test last name", 50,  group, faculty);
        teacherRepository.save(teacher);
        long teacherCount = teacherRepository.count();
        assertEquals(11, teacherCount);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void save_shouldThrowInvalidDataAccessApiUsageException_whenInputContainsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> teacherRepository.save(null));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findAll_shouldReturnCorrectTeacherList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        assertEquals(10, teacherList.size());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByLogin_shouldReturnTeacherWithCorrectLogin_whenInputContainsTeacherWithExistingLogin() {
        String login = "teacher_john_doe";
        Teacher teacher = teacherRepository.findTeacherByLogin(login);
        assertEquals(teacher.getLogin(), login);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByLogin_shouldReturnNull_whenInputContainsTeacherWithNotExistingLogin() {
        String login = "Test login";
        Teacher teacher = teacherRepository.findTeacherByLogin(login);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByLogin_shouldReturnNull_whenInputContainsNull() {
        Teacher teacher = teacherRepository.findTeacherByLogin(null);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByPhoneNumber_shouldReturnTeacherWithCorrectPhoneNumber_whenInputContainsTeacherWithExistingPhoneNumber() {
        String phoneNumber = "123456789";
        Teacher teacher = teacherRepository.findTeacherByPhoneNumber(phoneNumber);
        assertEquals(teacher.getPhoneNumber(), phoneNumber);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByPhoneNumber_shouldReturnNull_whenInputContainsTeacherWithNotExistingPhoneNumber() {
        String phoneNumber = "Test phone number";
        Teacher teacher = teacherRepository.findTeacherByPhoneNumber(phoneNumber);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByPhoneNumber_shouldReturnNull_whenInputContainsNull() {
        Teacher teacher = teacherRepository.findTeacherByPhoneNumber(null);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByEmail_shouldReturnTeacherWithCorrectEmail_whenInputContainsTeacherWithExistingEmail() {
        String email = "john_doe@example.com";
        Teacher teacher = teacherRepository.findTeacherByEmail(email);
        assertEquals(teacher.getEmail(), email);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByEmail_shouldReturnNull_whenInputContainsTeacherWithNotExistingEmail() {
        String email = "Test email";
        Teacher teacher = teacherRepository.findTeacherByEmail(email);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByEmail_shouldReturnNull_whenInputContainsNull() {
        Teacher teacher = teacherRepository.findTeacherByEmail(null);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByTeacherId_shouldReturnTeacherWithCorrectTeacherId_whenInputContainsTeacherWithExistingTeacherId() {
        long teacherId = 1;
        Teacher teacher = teacherRepository.findTeacherByTeacherId(teacherId);
        assertEquals(teacher.getTeacherId(), teacherId);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void findTeacherByTeacherId_shouldReturnNull_whenInputContainsTeacherWithNotExistingTeacherId() {
        long teacherId = 100;
        Teacher teacher = teacherRepository.findTeacherByTeacherId(teacherId);
        assertNull(teacher);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByLogin_shouldReturnTrue_whenInputContainsTeacherWithExistingLogin() {
        String login = "teacher_john_doe";
        boolean isLoginExists = teacherRepository.existsByLogin(login);
        assertTrue(isLoginExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByLogin_shouldReturnFalse_whenInputContainsTeacherWithNotExistingLogin() {
        String login = "Test login";
        boolean isLoginExists = teacherRepository.existsByLogin(login);
        assertFalse(isLoginExists);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByLogin_shouldReturnFalse_whenInputContainsNull() {
        boolean isLoginExists = teacherRepository.existsByLogin(null);
        assertFalse(isLoginExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByPhoneNumber_shouldReturnTrue_whenInputContainsTeacherWithExistingPhoneNumber() {
        String phoneNumber = "123456789";
        boolean isPhoneNumberExists = teacherRepository.existsByPhoneNumber(phoneNumber);
        assertTrue(isPhoneNumberExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByPhoneNumber_shouldReturnFalse_whenInputContainsTeacherWithNotExistingPhoneNumber() {
        String phoneNumber = "Test phone number";
        boolean isPhoneNumberExists = teacherRepository.existsByPhoneNumber(phoneNumber);
        assertFalse(isPhoneNumberExists);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByPhoneNumber_shouldReturnFalse_whenInputContainsNull() {
        boolean isPhoneNumberExists = teacherRepository.existsByPhoneNumber(null);
        assertFalse(isPhoneNumberExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByEmail_shouldReturnTrue_whenInputContainsTeacherWithExistingEmail() {
        String email = "john_doe@example.com";
        boolean isEmailExists = teacherRepository.existsByEmail(email);
        assertTrue(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsTeacherWithNotExistingEmail() {
        String email = "Test email";
        boolean isEmailExists = teacherRepository.existsByEmail(email);
        assertFalse(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByEmail_shouldReturnFalse_whenInputContainsNull() {
        boolean isEmailExists = teacherRepository.existsByEmail(null);
        assertFalse(isEmailExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByTeacherId_shouldReturnTrue_whenInputContainsTeacherWithExistingTeacherId() {
        long teacherId = 1;
        boolean isTeacherIdExists = teacherRepository.existsByTeacherId(teacherId);
        assertTrue(isTeacherIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_groups.sql", "/sql/insert_faculties.sql", "/sql/insert_teachers.sql"})
    void existsByTeacherId_shouldReturnFalse_whenInputContainsTeacherWithNotExistingTeacherId() {
        long teacherId = 100;
        boolean isTeacherIdExists = teacherRepository.existsByTeacherId(teacherId);
        assertFalse(isTeacherIdExists);
    }

}
