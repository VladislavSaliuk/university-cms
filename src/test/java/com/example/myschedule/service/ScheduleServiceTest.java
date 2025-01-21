package com.example.myschedule.service;

import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.dto.LessonDTO;
import com.example.myschedule.entity.*;
import com.example.myschedule.exception.*;
import com.example.myschedule.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;
    @Mock
    LessonRepository lessonRepository;
    @Mock
    GroupRepository groupRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    ClassroomRepository classroomRepository;
    @Mock
    UserRepository userRepository;
    Lesson lesson;
    LessonDTO lessonDTO;
    Group group;
    GroupDTO groupDTO;
    Course course;
    CourseDTO courseDTO;
    Classroom classroom;
    ClassroomDTO classroomDTO;
    @BeforeEach
    void setUp() {

        long courseId = 1L;
        String courseName = "Course name";
        String courseDescription = "Course description";

        course = Course.builder()
                .courseId(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        courseDTO = CourseDTO.builder()
                .courseId(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        long groupId = 1L;
        String groupName = "Group name";

        group = Group.builder()
                .groupId(groupId)
                .groupName(groupName)
                .build();

        groupDTO = GroupDTO.builder()
                .groupId(groupId)
                .groupName(groupName)
                .build();

        long classroomId = 1L;
        long classroomNumber = 1L;
        String classroomDescription = "Classroom description";

        classroom = Classroom.builder()
                .classroomId(classroomId)
                .classroomNumber(classroomNumber)
                .classroomDescription(classroomDescription)
                .build();

        classroomDTO = ClassroomDTO.builder()
                .classRoomId(classroomId)
                .classroomNumber(classroomNumber)
                .classroomDescription(classroomDescription)
                .build();

        long lessonId = 1L;
        LocalTime startTime = LocalTime.of(10,00);
        LocalTime endTime = LocalTime.of(11, 00);

        lesson = Lesson.builder()
                .lessonId(lessonId)
                .course(course)
                .group(group)
                .startTime(startTime)
                .endTime(endTime)
                .dayOfWeek(DayOfWeek.MONDAY)
                .classroom(classroom)
                .build();

        lessonDTO = LessonDTO.builder()
                .lessonId(lessonId)
                .courseDTO(courseDTO)
                .groupDTO(groupDTO)
                .startTime(startTime)
                .endTime(endTime)
                .dayOfWeek(DayOfWeek.MONDAY)
                .classroomDTO(classroomDTO)
                .build();

    }

    @Test
    void createLesson_shouldSaveLesson() {

        when(lessonRepository.findAll()).thenReturn(Collections.emptyList());

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.of(classroom));

        when(lessonRepository.save(any(Lesson.class)))
                .thenReturn(lesson);

        scheduleService.createLesson(lessonDTO);

        verify(lessonRepository).findAll();
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository).findById(lessonDTO.getClassroomDTO().getClassRoomId());
        verify(lessonRepository).save(any(Lesson.class));

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void createLesson_shouldThrowException_whenLessonTimesOverlap(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.MONDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        OverlapTimeException exception = assertThrows(OverlapTimeException.class, () -> scheduleService.createLesson(lessonDTO));
        assertEquals("The lesson overlaps with an existing lesson.", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(courseRepository,never()).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository,never()).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());
        verify(lessonRepository,never()).save(lesson);

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void createLesson_shouldThrowException_whenCourseNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);


        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> scheduleService.createLesson(lessonDTO));
        assertEquals("Course with " + lessonDTO.getCourseDTO().getCourseId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository,never()).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());
        verify(lessonRepository,never()).save(lesson);

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void createLesson_shouldThrowException_whenGroupNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.empty());

        ClassroomDTO newClassroomDTO = ClassroomDTO.builder()
                .classRoomId(2L)
                .classroomNumber(2L)
                .classroomDescription("Classroom description")
                .build();

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.MONDAY);
        lessonDTO.setClassroomDTO(newClassroomDTO);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> scheduleService.createLesson(lessonDTO));
        assertEquals("Group with " + lessonDTO.getGroupDTO().getGroupId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());
        verify(lessonRepository,never()).save(lesson);

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void createLesson_shouldThrowException_whenClassroomNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> scheduleService.createLesson(lessonDTO));
        assertEquals("Classroom with " + lessonDTO.getClassroomDTO().getClassRoomId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository).findById(lessonDTO.getClassroomDTO().getClassRoomId());
        verify(lessonRepository,never()).save(lesson);

    }

    @Test
    void updateLesson_shouldUpdateLesson() {

        when(lessonRepository.findAll())
                .thenReturn(Collections.emptyList());

        when(lessonRepository.findById(lessonDTO.getLessonId()))
                .thenReturn(Optional.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.of(classroom));

        lessonDTO.setStartTime(LocalTime.of(11, 00));
        lessonDTO.setEndTime(LocalTime.of(12, 00));

        scheduleService.updateLesson(lessonDTO);

        verify(lessonRepository).findAll();
        verify(lessonRepository).findById(lessonDTO.getLessonId());
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }
    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void updateLesson_shouldThrowException_whenLessonTimesOverlap(String startTime, String endTime) {

        when(lessonRepository.findAll())
                .thenReturn(List.of(lesson));

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.MONDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        OverlapTimeException exception = assertThrows(OverlapTimeException.class, () -> scheduleService.updateLesson(lessonDTO));
        assertEquals("The lesson overlaps with an existing lesson.", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(lessonRepository,never()).findById(lessonDTO.getLessonId());
        verify(courseRepository,never()).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository,never()).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void updateLesson_shouldThrowException_whenLessonNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll())
                .thenReturn(List.of(lesson));

        when(lessonRepository.findById(lessonDTO.getLessonId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        LessonNotFoundException exception = assertThrows(LessonNotFoundException.class, () -> scheduleService.updateLesson(lessonDTO));
        assertEquals("Lesson with " + lessonDTO.getLessonId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(lessonRepository).findById(lessonDTO.getLessonId());
        verify(courseRepository,never()).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository,never()).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void updateLesson_shouldThrowException_whenCourseNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(lessonRepository.findById(lessonDTO.getLessonId()))
                .thenReturn(Optional.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> scheduleService.updateLesson(lessonDTO));
        assertEquals("Course with " + lessonDTO.getCourseDTO().getCourseId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(lessonRepository).findById(lessonDTO.getLessonId());
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository,never()).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void updateLesson_shouldThrowException_whenGroupNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(lessonRepository.findById(lessonDTO.getLessonId()))
                .thenReturn(Optional.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> scheduleService.updateLesson(lessonDTO));
        assertEquals("Group with " + lessonDTO.getGroupDTO().getGroupId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(lessonRepository).findById(lessonDTO.getLessonId());
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository,never()).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }

    @ParameterizedTest
    @CsvSource({
            "10:30, 11:30",
            "09:30, 10:30",
            "10:00, 11:00",
            "09:45, 11:15"
    })
    void updateLesson_shouldThrowException_whenClassroomNotFound(String startTime, String endTime) {

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        when(lessonRepository.findById(lessonDTO.getLessonId()))
                .thenReturn(Optional.of(lesson));

        when(courseRepository.findById(courseDTO.getCourseId()))
                .thenReturn(Optional.of(course));

        when(groupRepository.findById(groupDTO.getGroupId()))
                .thenReturn(Optional.of(group));

        when(classroomRepository.findById(classroomDTO.getClassRoomId()))
                .thenReturn(Optional.empty());

        lessonDTO.setCourseDTO(courseDTO);
        lessonDTO.setGroupDTO(groupDTO);
        lessonDTO.setStartTime(LocalTime.parse(startTime));
        lessonDTO.setEndTime(LocalTime.parse(endTime));
        lessonDTO.setDayOfWeek(DayOfWeek.THURSDAY);
        lessonDTO.setClassroomDTO(classroomDTO);

        ClassroomNotFoundException exception = assertThrows(ClassroomNotFoundException.class, () -> scheduleService.updateLesson(lessonDTO));
        assertEquals("Classroom with " + lessonDTO.getClassroomDTO().getClassRoomId() + " Id not found!", exception.getMessage());

        verify(lessonRepository).findAll();
        verify(lessonRepository).findById(lessonDTO.getLessonId());
        verify(courseRepository).findById(lessonDTO.getCourseDTO().getCourseId());
        verify(groupRepository).findById(lessonDTO.getGroupDTO().getGroupId());
        verify(classroomRepository).findById(lessonDTO.getClassroomDTO().getClassRoomId());

    }
    @Test
    void getLessonById_shouldReturnLessonDTO() {

        long lessonId = 1L;

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.of(lesson));

        LessonDTO actualLessonDTO = scheduleService.getLessonById(lessonId);

        assertNotNull(actualLessonDTO);
        assertEquals(lessonDTO, actualLessonDTO);


        verify(lessonRepository).findById(lessonId);

    }
    @Test
    void getLessonById_shouldThrowException_whenLessonNotFound() {

        long lessonId = 100L;

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.empty());

        LessonNotFoundException exception = assertThrows(LessonNotFoundException.class, () -> scheduleService.getLessonById(lessonId));

        assertEquals("Lesson with " + lessonId + " Id not found!", exception.getMessage());

        verify(lessonRepository).findById(lessonId);

    }
    @Test
    void removeLessonById_shouldDeleteLesson() {

        long lessonId = 1L;

        when(lessonRepository.existsById(lessonId))
                .thenReturn(true);

        doNothing().when(lessonRepository).deleteById(lessonId);

        scheduleService.removeLessonById(lessonId);

        verify(lessonRepository).existsById(lessonId);
        verify(lessonRepository).deleteById(lessonId);

    }

    @Test
    void removeLessonById_shouldThrowException_whenLessonNotFound() {

        long lessonId = 100L;

        when(lessonRepository.existsById(lessonId))
                .thenReturn(false);

        LessonNotFoundException exception = assertThrows(LessonNotFoundException.class, () -> scheduleService.removeLessonById(lessonId));

        assertEquals("Lesson with " + lessonId + " Id not found!", exception.getMessage());

        verify(lessonRepository).existsById(lessonId);
        verify(lessonRepository, never()).deleteById(lessonId);

    }

    @Test
    void getScheduleForGroup_shouldReturnLessonDTOList() {

        long groupId = 1L;

        when(groupRepository.existsById(groupId))
                .thenReturn(true);

        when(lessonRepository.findAll())
                .thenReturn(List.of(lesson));

        List<LessonDTO> studentSchedule = scheduleService.getScheduleForGroup(groupId);

        assertNotNull(studentSchedule);
        assertFalse(studentSchedule.isEmpty());
        assertEquals(1, studentSchedule.size());

        verify(groupRepository).existsById(groupId);
        verify(lessonRepository).findAll();

    }

    @Test
    void getScheduleForGroup_shouldThrowException_whenGroupNotExist() {

        long groupId = 1L;

        when(groupRepository.existsById(groupId))
                .thenReturn(false);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () ->scheduleService.getScheduleForGroup(groupId));
        assertEquals("Group with " + groupId + " Id not found!", exception.getMessage());

        verify(groupRepository).existsById(groupId);
        verify(lessonRepository,never()).findAll();

    }

    @Test
    void getScheduleForTeacher_shouldReturnLessonDTOList() {

        long userId = 1L;

        User user = User.builder()
                .userId(userId)
                .role(Role.TEACHER)
                .build();

        course.setUser(user);
        lesson.setCourse(course);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(lessonRepository.findAll())
                .thenReturn(List.of(lesson));

        List<LessonDTO> teacherSchedule = scheduleService.getScheduleForTeacher(userId);

        assertNotNull(teacherSchedule);
        assertFalse(teacherSchedule.isEmpty());
        assertEquals(1, teacherSchedule.size());

        verify(userRepository).findById(userId);
        verify(lessonRepository).findAll();

    }
    @Test
    void getScheduleForTeacher_shouldThrowException_whenUserNotFound() {

        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> scheduleService.getScheduleForTeacher(userId));
        assertEquals("User with " + userId + " Id not found!", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(lessonRepository,never()).findAll();

    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STUDENT", "ADMIN", "STUFF"})
    void getScheduleForTeacher_shouldThrowException_whenUserIsNotTeacher(Role role) {

        long userId = 1L;

        User user = User.builder()
                .userId(userId)
                .role(role)
                .build();

        course.setUser(user);
        lesson.setCourse(course);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> scheduleService.getScheduleForTeacher(userId));

        assertEquals("User with " + userId + " Id is not a teacher!", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(lessonRepository, never()).findAll();

    }

}