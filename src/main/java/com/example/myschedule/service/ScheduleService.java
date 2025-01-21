package com.example.myschedule.service;

import com.example.myschedule.dto.LessonDTO;
import com.example.myschedule.entity.*;
import com.example.myschedule.exception.*;
import com.example.myschedule.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final LessonRepository lessonRepository;

    private final GroupRepository groupRepository;

    private final CourseRepository courseRepository;

    private final ClassroomRepository classroomRepository;

    private final UserRepository userRepository;

    public void createLesson(LessonDTO lessonDTO) {
        log.info("Attempting to create a new lesson: {}", lessonDTO);

        if (hasTimeOverlap(lessonDTO)) {
            log.warn("Lesson overlaps with an existing lesson: {}", lessonDTO);
            throw new OverlapTimeException("The lesson overlaps with an existing lesson.");
        }

        Course course = courseRepository.findById(lessonDTO.getCourseDTO().getCourseId()).orElseThrow(() -> {
            log.error("Course not found with ID: {}", lessonDTO.getCourseDTO().getCourseId());
            return new CourseNotFoundException("Course with " + lessonDTO.getCourseDTO().getCourseId() + " Id not found!");
        });

        Group group = groupRepository.findById(lessonDTO.getGroupDTO().getGroupId()).orElseThrow(() -> {
            log.error("Group not found with ID: {}", lessonDTO.getGroupDTO().getGroupId());
            return new GroupNotFoundException("Group with " + lessonDTO.getGroupDTO().getGroupId() + " Id not found!");
        });

        Classroom classroom = classroomRepository.findById(lessonDTO.getClassroomDTO().getClassRoomId()).orElseThrow(() -> {
            log.error("Classroom not found with ID: {}", lessonDTO.getClassroomDTO().getClassRoomId());
            return new ClassroomNotFoundException("Classroom with " + lessonDTO.getClassroomDTO().getClassRoomId() + " Id not found!");
        });

        Lesson lesson = Lesson.builder()
                .course(course)
                .group(group)
                .startTime(lessonDTO.getStartTime())
                .endTime(lessonDTO.getEndTime())
                .dayOfWeek(lessonDTO.getDayOfWeek())
                .classroom(classroom)
                .build();

        Lesson createdLesson = lessonRepository.save(lesson);
        log.info("Lesson created successfully: {}", createdLesson);

    }

    @Transactional
    public void updateLesson(LessonDTO lessonDTO) {
        log.info("Attempting to update lesson with ID: {}", lessonDTO.getLessonId());

        if (hasTimeOverlap(lessonDTO)) {
            log.warn("Updated lesson overlaps with an existing lesson: {}", lessonDTO);
            throw new OverlapTimeException("The lesson overlaps with an existing lesson.");
        }

        Lesson updatedLesson = lessonRepository.findById(lessonDTO.getLessonId()).orElseThrow(() -> {
            log.error("Lesson not found with ID: {}", lessonDTO.getLessonId());
            return new LessonNotFoundException("Lesson with " + lessonDTO.getLessonId() + " Id not found!");
        });

        Course course = courseRepository.findById(lessonDTO.getCourseDTO().getCourseId()).orElseThrow(() -> {
            log.error("Course not found with ID: {}", lessonDTO.getCourseDTO().getCourseId());
            return new CourseNotFoundException("Course with " + lessonDTO.getCourseDTO().getCourseId() + " Id not found!");
        });

        Group group = groupRepository.findById(lessonDTO.getGroupDTO().getGroupId()).orElseThrow(() -> {
            log.error("Group not found with ID: {}", lessonDTO.getGroupDTO().getGroupId());
            return new GroupNotFoundException("Group with " + lessonDTO.getGroupDTO().getGroupId() + " Id not found!");
        });

        Classroom classroom = classroomRepository.findById(lessonDTO.getClassroomDTO().getClassRoomId()).orElseThrow(() -> {
            log.error("Classroom not found with ID: {}", lessonDTO.getClassroomDTO().getClassRoomId());
            return new ClassroomNotFoundException("Classroom with " + lessonDTO.getClassroomDTO().getClassRoomId() + " Id not found!");
        });

        updatedLesson.setCourse(course);
        updatedLesson.setGroup(group);
        updatedLesson.setStartTime(lessonDTO.getStartTime());
        updatedLesson.setEndTime(lessonDTO.getEndTime());
        updatedLesson.setDayOfWeek(lessonDTO.getDayOfWeek());
        updatedLesson.setClassroom(classroom);

        log.info("Lesson updated successfully: {}", updatedLesson);
    }

    public LessonDTO getLessonById(long lessonId) {
        log.info("Fetching lesson with ID: {}", lessonId);

        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    log.info("Lesson found: {}", lesson);
                    return LessonDTO.toLessonDTO(lesson);
                })
                .orElseThrow(() -> {
                    log.error("Lesson not found with ID: {}", lessonId);
                    return new LessonNotFoundException("Lesson with " + lessonId + " Id not found!");
                });
    }

    public void removeLessonById(long lessonId) {
        log.info("Attempting to remove lesson with ID: {}", lessonId);

        if (!lessonRepository.existsById(lessonId)) {
            log.error("Lesson not found with ID: {}", lessonId);
            throw new LessonNotFoundException("Lesson with " + lessonId + " Id not found!");
        }

        lessonRepository.deleteById(lessonId);
        log.info("Lesson removed successfully with ID: {}", lessonId);
    }

    public List<LessonDTO> getScheduleForGroup(long groupId) {
        log.debug("Attempting to retrieve schedule for group with ID: {}", groupId);

        if (!groupRepository.existsById(groupId)) {
            log.error("Group with ID: {} not found!", groupId);
            throw new GroupNotFoundException("Group with " + groupId + " Id not found!");
        }
        log.info("Group with ID: {} found. Retrieving schedule.", groupId);

        List<LessonDTO> schedule = lessonRepository.findAll()
                .stream()
                .filter(lesson -> lesson.getGroup().getGroupId() == groupId)
                .map(LessonDTO::toLessonDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} lessons for group with ID: {}", schedule.size(), groupId);
        return schedule;
    }

    public List<LessonDTO> getScheduleForTeacher(long userId) {
        log.debug("Attempting to retrieve schedule for teacher with User ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID: {} not found!", userId);
            return new UserNotFoundException("User with " + userId + " Id not found!");
        });

        if (!user.getRole().name().equals(Role.TEACHER.name())) {
            log.error("User with ID: {} is not a teacher!", userId);
            throw new UserException("User with " + userId + " Id is not a teacher!");
        }
        log.info("User with ID: {} is a teacher. Retrieving schedule.", userId);

        List<LessonDTO> schedule = lessonRepository.findAll()
                .stream()
                .filter(lesson -> lesson.getCourse().getUser().getUserId() == userId)
                .map(LessonDTO::toLessonDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} lessons for teacher with User ID: {}", schedule.size(), userId);
        return schedule;
    }

    private boolean hasTimeOverlap(LessonDTO newLesson) {
        log.debug("Checking for time overlap for lesson: {}", newLesson);

        boolean overlap = lessonRepository.findAll().stream().anyMatch(existingLesson ->
                existingLesson.getDayOfWeek() == newLesson.getDayOfWeek()
                        &&
                        (existingLesson.getStartTime().isBefore(newLesson.getEndTime()))
                        &&
                        (existingLesson.getEndTime().isAfter(newLesson.getStartTime()))
                        &&
                        (existingLesson.getClassroom().getClassroomId() == newLesson.getClassroomDTO().getClassRoomId())
        );

        if (overlap) {
            log.warn("Time overlap detected for lesson: {}", newLesson);
        } else {
            log.debug("No time overlap detected for lesson: {}", newLesson);
        }

        return overlap;
    }
}