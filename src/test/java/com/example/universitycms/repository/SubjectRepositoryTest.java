package com.example.universitycms.repository;

import com.example.universitycms.model.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubjectRepositoryTest {


    @Autowired
    SubjectRepository subjectRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findAll_shouldReturnCorrectSubjectList() {
        List<Subject> subjectList = subjectRepository.findAll();
        assertEquals(10,subjectList.size());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findSubjectBySubjectName_shouldReturnSubjectWithCorrectSubjectName_whenInputContainsExistingSubjectName() {
        String subjectName = "Computer Science";
        Subject subject = subjectRepository.findSubjectBySubjectName(subjectName);
        assertEquals(subject.getSubjectName(), subjectName);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findSubjectBySubjectName_shouldReturnNull_whenInputContainsNotExistingSubjectName() {
        String subjectName = "Test subject name";
        Subject subject = subjectRepository.findSubjectBySubjectName(subjectName);
        assertNull(subject);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findSubjectBySubjectName_shouldReturnNull_whenInputContainsNull() {
        Subject subject = subjectRepository.findSubjectBySubjectName(null);
        assertNull(subject);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findSubjectBySubjectId_shouldReturnSubjectWithCorrectSubjectId_whenInputContainsExistingSubjectId() {
        long subjectId = 6;
        Subject subject = subjectRepository.findSubjectBySubjectId(subjectId);
        assertTrue(subject.getSubjectId() == subjectId);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void findSubjectBySubjectId_shouldReturnNull_whenInputContainsNotExistingSubjectId() {
        long subjectId = 100;
        Subject subject = subjectRepository.findSubjectBySubjectId(subjectId);
        assertNull(subject);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void existsBySubjectName_shouldReturnTrue_whenInputContainsExistingSubjectName() {
        String subjectName = "Computer Science";
        boolean isSubjectNameExists = subjectRepository.existsBySubjectName(subjectName);
        assertTrue(isSubjectNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void existsBySubjectName_shouldReturnFalse_whenInputContainsNotExistingSubjectName() {
        String subjectName = "Test subject name";
        boolean isSubjectNameExists = subjectRepository.existsBySubjectName(subjectName);
        assertFalse(isSubjectNameExists);
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void existsBySubjectName_shouldReturnFalse_whenInputContainsNull() {
        boolean isSubjectNameExists = subjectRepository.existsBySubjectName(null);
        assertFalse(isSubjectNameExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void existsBySubjectId_shouldReturnTrue_whenInputContainsExistingSubjectId() {
        long subjectId = 4;
        boolean isSubjectIdExists = subjectRepository.existsBySubjectId(subjectId);
        assertTrue(isSubjectIdExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_subjects.sql"})
    void existsBySubjectId_shouldReturnFalse_whenInputContainsNotExistingSubjectId() {
        long subjectId = 404;
        boolean isSubjectIdExists = subjectRepository.existsBySubjectId(subjectId);
        assertFalse(isSubjectIdExists);
    }


}
