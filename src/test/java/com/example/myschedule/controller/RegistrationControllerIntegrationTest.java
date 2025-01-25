package com.example.myschedule.controller;


import com.example.myschedule.dto.RegistrationDTO;
import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_groups.sql", "/sql/insert_users.sql"})
public class RegistrationControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    RegistrationDTO registrationDTO;

    @Autowired
    MockMvc mockMvc;
    @BeforeEach
    void setUp() {

        String username = "username";
        String password = "Testpas_sword1";
        String email = "email@gmail.com";
        String firstname = "Firstname";
        String lastname = "Lastname";

        registrationDTO = RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .role(Role.STUDENT)
                .status(Status.ACTIVE)
                .build();

    }

    @Test
    void showRegistrationPage_shouldReturnRegistrationPageView() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-page"))
                .andReturn();
    }

    @Test
    void showRegisterStudentPage_shouldReturnRegisterStudentPageView() throws Exception {
        mockMvc.perform(get("/registration/register-student"))
                .andExpect(model().attributeExists("registrationDTO"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andReturn();
    }

    @Test
    void showRegisterTeacherPage_shouldReturnRegisterTeacherPageView() throws Exception {
        mockMvc.perform(get("/registration/register-teacher"))
                .andExpect(model().attributeExists("registrationDTO"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andReturn();
    }

    @Test
    void registerStudent_shouldRedirectToLoginPage() throws Exception {

        MvcResult result = mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Welcome, " + registrationDTO.getUsername() + "!", successMessage);

    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenFirstnameIsEmpty() throws Exception {

       registrationDTO.setFirstname("");

       mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Firstname should not be empty!")))
                .andReturn();


    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenFirstnameIsNull() throws Exception {

        registrationDTO.setFirstname(null);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Firstname should not be empty!")))
                .andReturn();


    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John123",
            "John Doe",
            "John_Doe",
            "@John",
            "123",
            "Jöhn",
            "Doe!",
            " John",
            "Doe ",
            "Влад",
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenFirstnameContainsNotLatinCharacters(String firstname) throws Exception {

        registrationDTO.setFirstname(firstname);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Firstname should contain only Latin characters!")))
                .andReturn();


    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenLastnameIsEmpty() throws Exception {

        registrationDTO.setLastname("");

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Lastname should not be empty!")))
                .andReturn();


    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenLastnameIsNull() throws Exception {

        registrationDTO.setLastname(null);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Lastname should not be empty!")))
                .andReturn();


    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Smith123",
            "O'Connor",
            "Van Dyke",
            "Doe_Jr",
            "@Wilson",
            "123456",
            "Müller",
            " Ivanov",
            "Petrov ",
            "Лисенко"
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenLastnameContainsNotLatinCharacters(String lastname) throws Exception {

        registrationDTO.setLastname(lastname);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Lastname should contain only Latin characters!")))
                .andReturn();


    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenUsernameIsEmpty() throws Exception {

        registrationDTO.setUsername("");

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Username should not be empty!")))
                .andReturn();


    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenUsernameIsNull() throws Exception {

        registrationDTO.setUsername(null);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Username should not be empty!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John_Doe",
            "user@name",
            "username!",
            "user name",
            "123 456",
            "user.name",
            "name#",
            "user-name",
            " name",
            "name ",
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenUsernameContainsSpecialCharacters(String username) throws Exception {

        registrationDTO.setUsername(username);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Username should contain only Latin characters and numbers!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc",
            "abcd",
            "a",
            "abcdefghijklmnop",
            "averyverylongusername",
            "u12",
            "u1",
            "user",
            "toolongusernameforvalidation",
            "usr"
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenUsernameLengthTooShortOrTooLong(String username) throws Exception {

        registrationDTO.setUsername(username);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Username should be between 5 and 15 characters!")))
                .andReturn();

    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenEmailIsEmpty() throws Exception {

        registrationDTO.setEmail("");

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("E-mail should not be empty!")))
                .andReturn();

    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenEmailIsNull() throws Exception {

        registrationDTO.setEmail(null);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("E-mail should not be empty!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@no-local-part.com",
            "Outlook@.com",
            "usernamedomain",
            "username@domain,com",
            "username@domain..com",
            "username@-domain.com",
            "username@domain@domain.com",
            "usernamedomain.c",
            "username@.com"
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenEmailNotValid(String email) throws Exception {

        registrationDTO.setEmail(email);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("E-mail should be valid!")))
                .andReturn();

    }

    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenPasswordIsEmpty() throws Exception {

        registrationDTO.setPassword("");

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Password should not be empty!")))
                .andReturn();


    }
    @Test
    void registerStudent_shouldReturnRegisterStudentPageView_whenPasswordIsNull() throws Exception {

        registrationDTO.setPassword(null);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Password should not be empty!")))
                .andReturn();

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "password",
            "PASSWORD123",
            "Password123",
            "password123!",
            "PASSWORD!",
            "Pass123",
            "12345678!",
            "Password!",
            "Pass!word",
            "1234!5678"
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenPasswordNotContainsSpecialCharacters(String password) throws Exception {

        registrationDTO.setPassword(password);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Sho1_",
            "S12*4567S12*4567S12*4567",
            "Toolongpasswordexample1_",
            "Pwd1_",
            "Passwordpasswordpasswo2!_",
            "Abc123_",
            "Aq13^",
            "S12345678901s_34567"
    })
    void registerStudent_shouldReturnRegisterStudentPageView_whenPasswordTooLongRoTooShort(String password) throws Exception {

        registrationDTO.setPassword(password);

        mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(content().string(containsString("Password must be 8-16 characters long!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "johndoe", "janesmith", "marktwain", "lucyadams", "mikejones",
            "susanclark", "admin", "stuff", "emmabrown", "oliverblack"})
    void registerStudent_shouldReturnRegisterStudentPageView_whenUsernameRepeats(String username) throws Exception {

        registrationDTO.setUsername(username);

        MvcResult result = mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + registrationDTO.getUsername() + " username already exists!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john.doe@example.com", "jane.smith@example.com", "mark.twain@example.com",
            "lucy.adams@example.com", "mike.jones@example.com",
            "susan.clark@example.com", "salukvladislav81@gmail.com",
            "seattlemusic345@gmail.com", "emma.brown@example.com", "oliver.black@example.com"})
    void registerStudent_shouldReturnRegisterStudentPageView_whenEmailRepeats(String email) throws Exception {

        registrationDTO.setEmail(email);

        MvcResult result = mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + registrationDTO.getEmail() + " E-mail already exists!", errorMessage);

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN", "STUFF"})
    void registerStudent_shouldReturnRegisterStudentPageView_whenRoleNotAvailable(Role role) throws Exception {

        registrationDTO.setRole(role);

        MvcResult result = mockMvc.perform(post("/registration/register-student")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-student-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("This role is not allowed for you!", errorMessage);

    }

    @Test
    void registerTeacher_shouldRedirectToLoginPage() throws Exception {

        registrationDTO.setRole(Role.TEACHER);

        MvcResult result = mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("successMessage"))
                .andReturn();

        String successMessage = (String) result.getFlashMap().get("successMessage");
        assertNotNull(successMessage);
        assertEquals("Welcome, " + registrationDTO.getUsername() + "!", successMessage);

    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenFirstnameIsEmpty() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setFirstname("");

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Firstname should not be empty!")))
                .andReturn();


    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenFirstnameIsNull() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setFirstname(null);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Firstname should not be empty!")))
                .andReturn();


    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John123",
            "John Doe",
            "John_Doe",
            "@John",
            "123",
            "Jöhn",
            "Doe!",
            " John",
            "Doe ",
            "Влад",
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenFirstnameContainsNotLatinCharacters(String firstname) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setFirstname(firstname);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Firstname should contain only Latin characters!")))
                .andReturn();


    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenLastnameIsEmpty() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setLastname("");

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Lastname should not be empty!")))
                .andReturn();


    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenLastnameIsNull() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setLastname(null);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Lastname should not be empty!")))
                .andReturn();


    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Smith123",
            "O'Connor",
            "Van Dyke",
            "Doe_Jr",
            "@Wilson",
            "123456",
            "Müller",
            " Ivanov",
            "Petrov ",
            "Лисенко"
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenLastnameContainsNotLatinCharacters(String lastname) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setLastname(lastname);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Lastname should contain only Latin characters!")))
                .andReturn();


    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenUsernameIsEmpty() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setUsername("");

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Username should not be empty!")))
                .andReturn();


    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenUsernameIsNull() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setUsername(null);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Username should not be empty!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John_Doe",
            "user@name",
            "username!",
            "user name",
            "123 456",
            "user.name",
            "name#",
            "user-name",
            " name",
            "name ",
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenUsernameContainsSpecialCharacters(String username) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setUsername(username);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Username should contain only Latin characters and numbers!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc",
            "abcd",
            "a",
            "abcdefghijklmnop",
            "averyverylongusername",
            "u12",
            "u1",
            "user",
            "toolongusernameforvalidation",
            "usr"
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenUsernameLengthTooShortOrTooLong(String username) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setUsername(username);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Username should be between 5 and 15 characters!")))
                .andReturn();

    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenEmailIsEmpty() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setEmail("");

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("E-mail should not be empty!")))
                .andReturn();

    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenEmailIsNull() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setEmail(null);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("E-mail should not be empty!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@no-local-part.com",
            "Outlook@.com",
            "usernamedomain",
            "username@domain,com",
            "username@domain..com",
            "username@-domain.com",
            "username@domain@domain.com",
            "usernamedomain.c",
            "username@.com"
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenEmailNotValid(String email) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setEmail(email);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("E-mail should be valid!")))
                .andReturn();

    }

    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenPasswordIsEmpty() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setPassword("");

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Password should not be empty!")))
                .andReturn();


    }
    @Test
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenPasswordIsNull() throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setPassword(null);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Password should not be empty!")))
                .andReturn();

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "password",
            "PASSWORD123",
            "Password123",
            "password123!",
            "PASSWORD!",
            "Pass123",
            "12345678!",
            "Password!",
            "Pass!word",
            "1234!5678"
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenPasswordNotContainsSpecialCharacters(String password) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setPassword(password);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Sho1_",
            "S12*4567S12*4567S12*4567",
            "Toolongpasswordexample1_",
            "Pwd1_",
            "Passwordpasswordpasswo2!_",
            "Abc123_",
            "Aq13^",
            "S12345678901s_34567"
    })
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenPasswordTooLongRoTooShort(String password) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setPassword(password);

        mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(content().string(containsString("Password must be 8-16 characters long!")))
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "johndoe", "janesmith", "marktwain", "lucyadams", "mikejones",
            "susanclark", "admin", "stuff", "emmabrown", "oliverblack"})
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenUsernameRepeats(String username) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setUsername(username);

        MvcResult result = mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + registrationDTO.getUsername() + " username already exists!", errorMessage);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john.doe@example.com", "jane.smith@example.com", "mark.twain@example.com",
            "lucy.adams@example.com", "mike.jones@example.com",
            "susan.clark@example.com", "salukvladislav81@gmail.com",
            "seattlemusic345@gmail.com", "emma.brown@example.com", "oliver.black@example.com"})
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenEmailRepeats(String email) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setEmail(email);

        MvcResult result = mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("User with " + registrationDTO.getEmail() + " E-mail already exists!", errorMessage);

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN", "STUFF"})
    void registerTeacher_shouldReturnRegisterTeacherPageView_whenRoleNotAvailable(Role role) throws Exception {

        registrationDTO.setRole(Role.TEACHER);
        registrationDTO.setRole(role);

        MvcResult result = mockMvc.perform(post("/registration/register-teacher")
                        .flashAttr("registrationDTO", registrationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("register-teacher-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        String errorMessage = (String) result.getModelAndView().getModel().get("errorMessage");
        assertNotNull(errorMessage);
        assertEquals("This role is not allowed for you!", errorMessage);

    }

}