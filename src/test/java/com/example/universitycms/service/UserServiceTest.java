package com.example.universitycms.service;


import com.example.universitycms.model.*;
import com.example.universitycms.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {


    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    static List<User> userList = new LinkedList<>();

    @BeforeAll
    static void init() {
        userList.add(new User("Test username 1", "Test password 1", "Test email 1", new Role("USER")));
        userList.add(new User("Test username 2", "Test password 2", "Test email 2", new Role("USER")));
        userList.add(new User("Test username 3", "Test password 3", "Test email 3", new Role("USER")));
        userList.add(new User("Test username 4", "Test password 4", "Test email 4", new Role("USER")));
        userList.add(new User("Test username 5", "Test password 5", "Test email 5", new Role("USER")));
    }
    @Test
    void registerUser_shouldInsertUserToDatabase_whenInputContainsUser() {
        User user = new User("Test username", "Test password", "Test email", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName()))
                .thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail()))
                .thenReturn(false);
        userService.registerUser(user);
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }
    @Test
    void registerUser_shouldThrowException_whenInputContainsUserWithExistingUserName() {
        User user = new User("Test username 1", "Test password", "Test email", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("This username is already exists!", exception.getMessage());
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository,never()).existsByEmail(user.getEmail());
        verify(userRepository,never()).save(user);
    }

    @Test
    void registerUser_shouldThrowException_whenInputContainsUserWithExistingEmail() {
        User user = new User("Test username", "Test password", "Test email 1", new Role("USER"));
        when(userRepository.existsByUserName(user.getUserName()))
                .thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("This E-mail is already exists!", exception.getMessage());
        verify(userRepository).existsByUserName(user.getUserName());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void registerUser_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
        assertEquals("Input contains null!", exception.getMessage());
        verify(userRepository, never()).existsByUserName(null);
        verify(userRepository, never()).existsByEmail(null);
        verify(userRepository, never()).save(null);
    }

    @Test
    void getAll_shouldReturnUserList() {
        when(userRepository.findAll())
                .thenReturn(userList);
        List<User> actualUserList = userService.getAll();
        assertEquals(5, actualUserList.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUsersByRoleId_shouldReturnUserWithCorrectRole_whenInputContainsExistingRoleId() {
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("USER");

        User user = new User();
        user.setRole(role);

        List<User> userList = Arrays.asList(user);

        when(userRepository.existsByRole_RoleId(role.getRoleId())).thenReturn(true);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> actualUserList = userService.getUsersByRole(role.getRoleId());
        assertEquals(userList, actualUserList);
        verify(userRepository).existsByRole_RoleId(role.getRoleId());
        verify(userRepository).findAll();
    }

    @Test
    void getUsersByRoleId_shouldThrowException_whenInputContainsExistingRoleId() {
        Role role = new Role();
        role.setRoleId(100);
        role.setRoleName("Test user");

        User user = new User();
        user.setRole(role);

        when(userRepository.existsByRole_RoleId(role.getRoleId()))
                .thenReturn(false)
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUsersByRole(role.getRoleId()));
        assertEquals(exception.getMessage(), "This role doesn't exist!");
        verify(userRepository).existsByRole_RoleId(role.getRoleId());
        verify(userRepository, never()).findAll();
    }


    @Test
    void getUserByUsername_shouldThrowException_whenInputContainsNotExistingUsername() {

        String username = "Test username";

        when(userRepository.findUserByUserName(username))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername(username));

        assertEquals("User with this name doesn't exist!", exception.getMessage());

        verify(userRepository).findUserByUserName(username);
    }

    @Test
    void getUserByUsername_shouldReturnUser_whenInputContainsExistingUsername() {

        String username = "Test username";

        User expectedUser = new User();

        expectedUser.setUserId(1);
        expectedUser.setUserName(username);
        expectedUser.setPassword("Test password");
        expectedUser.setEmail("Test E-mail");
        expectedUser.setRole(RoleId.STUDENT.getValue());

        when(userRepository.findUserByUserName(username))
                .thenReturn(expectedUser);

        User actualUser = userService.getUserByUsername(username);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);

        verify(userRepository).findUserByUserName(username);
    }

    @Test
    void changeUserRole_shouldThrowException_whenInputContainsNotExistingUser() {

        long userId = 100;

        User user = new User();

        user.setUserId(userId);
        user.setUserName("Test username");
        user.setPassword("Test password");
        user.setEmail("Test E-mail");
        user.setRole(RoleId.STUDENT.getValue());
        user.setUserStatus(UserStatusId.ACTIVE.getValue());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.changeUserRole(user));

        assertEquals("This user doesn't exist!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(userRepository,never()).save(user);
    }

    @Test
    void changeUserRole_shouldUpdateUser_whenInputContainsCorrectUser() {

        long userId = 1;

        User existingUser = new User();

        existingUser.setUserId(userId);
        existingUser.setUserName("Test username");
        existingUser.setPassword("Test password");
        existingUser.setEmail("Test E-mail");
        existingUser.setRole(RoleId.ADMIN.getValue());
        existingUser.setUserStatus(UserStatusId.ACTIVE.getValue());


        User updatedUser = new User();

        updatedUser.setUserId(userId);
        updatedUser.setUserName("Test username");
        updatedUser.setPassword("Test password");
        updatedUser.setEmail("Test E-mail");
        updatedUser.setRole(RoleId.STUFF.getValue());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(existingUser);

        userService.changeUserRole(updatedUser);

        verify(userRepository).findUserByUserId(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    void changeUserStatus_shouldThrowException_whenInputContainsNotExistingUser() {

        long userId = 1;

        User user = new User();

        user.setUserId(userId);
        user.setUserStatus(UserStatusId.ACTIVE.getValue());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.changeUserStatus(user));

        assertEquals("This user doesn't exist!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
        verify(userRepository, never()).save(user);
    }
    @Test
    void changeUserStatus_shouldReturnUserStatus_whenInputContainsCorrectUser() {

        long userId = 1;

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUserStatus(UserStatusId.ACTIVE.getValue());

        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setUserStatus(UserStatusId.BANNED.getValue());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(existingUser);

        userService.changeUserStatus(expectedUser);

        verify(userRepository).findUserByUserId(userId);
        verify(userRepository).save(expectedUser);
    }
    @Test
    void getAllCoursesForStudentByUserId_shouldReturnCourseList_whenInputContainsExistingUserId() {

        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    Group group = new Group();
                    group.setCourseList(Collections.emptyList());
                    user.setGroup(group);
                    user.setCourseList(List.of(new Course()));
                    return user;
                })
                .collect(Collectors.toList());

        long userId = 4;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(userList.get((int)userId));

        List<Course> courseList = userService.getAllCoursesForStudentByUserId(userId);

        assertTrue(courseList.equals(Collections.emptyList()));
        verify(userRepository).findUserByUserId(userId);
    }

    @Test
    void getAllCoursesForStudentByUserId_shouldReturnEmptyCourseList_whenInputContainsUserWithOutGroup() {

        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setCourseList(List.of(new Course()));
                    return user;
                })
                .collect(Collectors.toList());

        long userId = 4;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(userList.get((int)userId));

        List<Course> courseList = userService.getAllCoursesForStudentByUserId(userId);

        assertTrue(courseList.equals(Collections.emptyList()));
        verify(userRepository).findUserByUserId(userId);
    }
    @Test
    void getAllCoursesForStudentByUserId_shouldThrowException_whenInputContainsNotExistingUserId() {

        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    Group group = new Group();
                    group.setCourseList(Collections.emptyList());
                    user.setGroup(group);
                    user.setCourseList(List.of(new Course()));
                    return user;
                })
                .collect(Collectors.toList());

        long userId = 100;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getAllCoursesForStudentByUserId(userId));

        assertEquals(exception.getMessage(), "User with this Id doesn't exist!");
        verify(userRepository).findUserByUserId(userId);
    }


    @Test
    void getAllCoursesForTeacherByUserId_shouldReturnCourseList_whenInputContainsExistingUserId() {

        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setCourseList(List.of(new Course()));
                    return user;
                })
                .collect(Collectors.toList());

        long userId = 4;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(userList.get((int)userId));

        List<Course> courseList = userService.getAllCoursesForTeacherByUserId(userId);

        assertTrue(courseList.equals(List.of(new Course())));
        verify(userRepository).findUserByUserId(userId);
    }
    @Test
    void getAllCoursesForTeacherByUserId_shouldThrowException_whenInputContainsNotExistingUserId() {

        List<User> userList = LongStream.range(0, 10)
                .mapToObj(userId -> {
                    User user = new User();
                    user.setUserId(userId);
                    user.setCourseList(List.of(new Course()));
                    return user;
                })
                .collect(Collectors.toList());

        long userId = 100;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getAllCoursesForTeacherByUserId(userId));

        assertEquals(exception.getMessage(), "User with this Id doesn't exist!");
        verify(userRepository).findUserByUserId(userId);
    }


    @Test
    void getScheduleForStudent_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getScheduleForStudent(userId));

        assertEquals("User with this Id does not exist!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
    }

    @Test
    void getScheduleForStudent_shouldThrowException_whenInputContainsUserIdNotBelongStudent() {

        long userId = 5;

        User user = new User();

        user.setUserId(userId);
        user.setUserName("Test username");
        user.setPassword("Test password");
        user.setEmail("Test E-mail");
        user.setRole(RoleId.TEACHER.getValue());

        when(userRepository.findUserByUserId(user.getUserId()))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getScheduleForStudent(user.getUserId()));

        assertEquals("Your user is not a student!", exception.getMessage());

        verify(userRepository).findUserByUserId(user.getUserId());
    }

    @Test
    void getScheduleForStudent_shouldReturnCourseList_whenInputContainsUserId() {

        List<Course> allCourses = new LinkedList<>();

        allCourses.add(new Course("Test course name 1", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("08:30"), LocalTime.parse("10:00")));
        allCourses.add(new Course("Test course name 2", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("10:30"), LocalTime.parse("12:00")));
        allCourses.add(new Course("Test course name 3", "description"));
        allCourses.add(new Course("Test course name 4", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("14:30"), LocalTime.parse("16:00")));
        allCourses.add(new Course("Test course name 5", "description"));

        Group group = new Group();
        group.setGroupName("Test group name");
        group.setCourseList(allCourses);

        long userId = 5;

        User user = new User();

        user.setUserId(userId);
        user.setUserName("Test username");
        user.setPassword("Test password");
        user.setEmail("Test E-mail");
        user.setRole(RoleId.STUDENT.getValue());
        user.setGroup(group);

        when(userRepository.findUserByUserId(user.getUserId()))
                .thenReturn(user);

        List<Course> scheduleCourseList = userService.getScheduleForStudent(user.getUserId());

        assertFalse(scheduleCourseList.isEmpty());
        assertEquals(3, scheduleCourseList.size());

        verify(userRepository).findUserByUserId(user.getUserId());
    }

    @Test
    void getScheduleForTeacher_shouldThrowException_whenInputContainsNotExistingUserId() {

        long userId = 100;

        when(userRepository.findUserByUserId(userId))
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getScheduleForTeacher(userId));

        assertEquals("User with this Id does not exist!", exception.getMessage());

        verify(userRepository).findUserByUserId(userId);
    }

    @Test
    void getScheduleForTeacher_shouldThrowException_whenInputContainsUserIdNotBelongTeacher() {

        long userId = 5;

        User user = new User();
        user.setUserId(userId);
        user.setUserName("Test username");
        user.setPassword("Test password");
        user.setEmail("Test E-mail");
        user.setRole(RoleId.STUDENT.getValue());

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getScheduleForTeacher(user.getUserId()));

        assertEquals("Your user is not a teacher!", exception.getMessage());

        verify(userRepository).findUserByUserId(user.getUserId());
    }

    @Test
    void getScheduleForTeacher_shouldReturnCourseList_whenInputContainsUserId() {

        List<Course> allCourses = new LinkedList<>();

        allCourses.add(new Course("Test course name 1", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("08:30"), LocalTime.parse("10:00")));
        allCourses.add(new Course("Test course name 2", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("10:30"), LocalTime.parse("12:00")));
        allCourses.add(new Course("Test course name 3", "description"));
        allCourses.add(new Course("Test course name 4", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("14:30"), LocalTime.parse("16:00")));
        allCourses.add(new Course("Test course name 5", "description", DayOfWeek.MONDAY.getValue(), LocalTime.parse("16:30"), LocalTime.parse("18:00")));

        long userId = 5;

        User user = new User();

        user.setUserId(userId);
        user.setUserName("Test username");
        user.setPassword("Test password");
        user.setEmail("Test E-mail");
        user.setRole(RoleId.TEACHER.getValue());
        user.setCourseList(allCourses);

        when(userRepository.findUserByUserId(userId))
                .thenReturn(user);

        List<Course> scheduleCourseList = userService.getScheduleForTeacher(user.getUserId());

        assertFalse(scheduleCourseList.isEmpty());
        assertEquals(4, scheduleCourseList.size());

        verify(userRepository).findUserByUserId(userId);
    }

}
