package com.example.universitycms.repository;

import com.example.universitycms.model.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FacultyRepositoryTest {

    @Autowired
    FacultyRepository facultyRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findAll_shouldReturnFacultyList(){
        List<Faculty> facultyList = facultyRepository.findAll();
        assertEquals(10, facultyList.size());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findFacultyByFacultyName_shouldReturnFaculty_whenInputContainsExistingFacultyName(){
        String facultyName = "Arts";
        Faculty faculty = facultyRepository.findFacultyByFacultyName(facultyName);
        assertTrue(faculty.getFacultyName().equals(facultyName));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findFacultyByFacultyName_shouldReturnNull_whenInputContainsNotExistingFacultyName(){
        String facultyName = "Test faculty name";
        Faculty faculty = facultyRepository.findFacultyByFacultyName(facultyName);
        assertNull(faculty);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findFacultyByFacultyName_shouldReturnNull_whenInputContainsNull(){
        Faculty faculty = facultyRepository.findFacultyByFacultyName(null);
        assertNull(faculty);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findFacultyByFacultyId_shouldReturnFaculty_whenInputContainsExistingFacultyId(){
        long facultyId = 6;
        Faculty faculty = facultyRepository.findFacultyByFacultyId(facultyId);
        assertTrue(faculty.getFacultyId() == facultyId);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void findFacultyByFacultyId_shouldReturnNull_whenInputContainsNotExistingFacultyId(){
        long facultyId = 100;
        Faculty faculty = facultyRepository.findFacultyByFacultyId(facultyId);
        assertNull(faculty);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void existsByFacultyName_shouldReturnTrue_whenInputContainsExistingFacultyName() {
        String facultyName = "Computer Science";
        boolean isFacultyNameExists = facultyRepository.existsByFacultyName(facultyName);
        assertTrue(isFacultyNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void existsByFacultyName_shouldReturnFalse_whenInputContainsNotExistingFacultyName() {
        String facultyName = "Test faculty name";
        boolean isFacultyNameExists = facultyRepository.existsByFacultyName(facultyName);
        assertFalse(isFacultyNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void existsByFacultyName_shouldReturnNull_whenInputContainsNull() {
        boolean isFacultyNameExists = facultyRepository.existsByFacultyName(null);
        assertFalse(isFacultyNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void existsByFacultyId_shouldReturnTrue_whenInputContainsExistingFacultyId() {
        long facultyId = 7;
        boolean isFacultyIdExists = facultyRepository.existsByFacultyId(facultyId);
        assertTrue(isFacultyIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_faculties.sql"})
    void existsByFacultyId_shouldReturnFalse_whenInputContainsNotExistingFacultyId() {
        long facultyId = 100;
        boolean isFacultyIdExists = facultyRepository.existsByFacultyId(facultyId);
        assertFalse(isFacultyIdExists);
    }

}
