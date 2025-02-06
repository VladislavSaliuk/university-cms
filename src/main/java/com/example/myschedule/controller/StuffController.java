package com.example.myschedule.controller;

import com.example.myschedule.converter.*;
import com.example.myschedule.dto.*;
import com.example.myschedule.editor.*;
import com.example.myschedule.entity.DayOfWeek;
import com.example.myschedule.exception.*;
import com.example.myschedule.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StuffController {

    private final ClassroomService classroomService;

    private final CourseService courseService;

    private final GroupService groupService;

    private final ScheduleService scheduleService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final GroupDTOConverter groupDTOConverter;

    private final TeacherDTOConverter teacherDTOConverter;

    private final CourseDTOConverter courseDTOConverter;

    private final ClassroomDTOConverter classroomDTOConverter;

    private final DayOfWeekConverter dayOfWeekConverter;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(GroupDTO.class, new GroupDTOPropertyEditor(groupDTOConverter));
        binder.registerCustomEditor(TeacherDTO.class, new TeacherDTOPropertyEditor(teacherDTOConverter));
        binder.registerCustomEditor(CourseDTO.class, new CourseDTOPropertyEditor(courseDTOConverter));
        binder.registerCustomEditor(ClassroomDTO.class, new ClassroomDTOPropertyEditor(classroomDTOConverter));
        binder.registerCustomEditor(DayOfWeek.class, new DayOfWeekPropertyEditor(dayOfWeekConverter));
    }
    @GetMapping("/stuff")
    public String showStuffHomePage() {
        return "stuff-home-page";
    }
    @GetMapping("/stuff/classrooms-dashboard")
    public String showStuffClassroomsDashboardPage(Model model) {
        model.addAttribute("classroomList", classroomService.getAll());
        model.addAttribute("classroomDTO", new ClassroomDTO());
        return "stuff-classrooms-dashboard-page";
    }

    @GetMapping("/stuff/courses-dashboard")
    public String showStuffCoursesDashboardPage(Model model) {
        model.addAttribute("courseList", courseService.getAll());
        model.addAttribute("teacherList", teacherService.getAllTeachers());
        model.addAttribute("courseDTO", new CourseDTO());
        return "stuff-courses-dashboard-page";
    }

    @GetMapping("/stuff/groups-dashboard")
    public String showStuffGroupsDashboardPage(Model model) {
        model.addAttribute("groupList", groupService.getAll());
        model.addAttribute("groupDTO", new GroupDTO());
        return "stuff-groups-dashboard-page";
    }

    @GetMapping("/stuff/lessons-dashboard")
    public String showStuffLessonsDashboardPage(Model model) {
        model.addAttribute("lessonList", scheduleService.getAllLessons());
        model.addAttribute("courseList", courseService.getAll());
        model.addAttribute("groupList", groupService.getAll());
        model.addAttribute("classroomList", classroomService.getAll());
        model.addAttribute("lessonDTO", new LessonDTO());
        return "stuff-lessons-dashboard-page";
    }

    @GetMapping("/stuff/students-dashboard")
    public String showStuffStudentsDashboardPage(Model model) {
        model.addAttribute("studentList", studentService.getAllStudents());
        model.addAttribute("groupList", groupService.getAll());
        model.addAttribute("studentDTO", new StudentDTO());
        return "stuff-students-dashboard-page";
    }
    @GetMapping("/stuff/teachers-dashboard")
    public String showStuffTeachersDashboardPage(Model model) {
        model.addAttribute("teacherList", teacherService.getAllTeachers());
        return "stuff-teachers-dashboard-page";
    }

    @PostMapping("/stuff/classrooms-dashboard/create")
    public String createClassroom(@Valid @ModelAttribute("classroomDTO") ClassroomDTO classroomDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/classrooms-dashboard";
            }

            classroomService.createClassroom(classroomDTO);

            String successMessage = "Classroom created successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/classrooms-dashboard";
        } catch (ClassroomException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/classrooms-dashboard";
        }
    }
    @PostMapping("/stuff/classrooms-dashboard/update")
    public String updateClassroom(@Valid @ModelAttribute("classroomDTO") ClassroomDTO classroomDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/classrooms-dashboard";
            }

            classroomService.updateClassroom(classroomDTO);

            String successMessage = "Classroom updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/classrooms-dashboard";
        } catch (ClassroomException | ClassroomNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/classrooms-dashboard";
        }
    }

    @GetMapping("/stuff/classrooms-dashboard/delete")
    public String removeClassroomById(@RequestParam long classroomId, RedirectAttributes redirectAttributes) {
        try {

            classroomService.removeById(classroomId);

            String successMessage = "Classroom deleted successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/classrooms-dashboard";
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Cannot delete classroom because it is associated with existing lessons.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/stuff/classrooms-dashboard";
        } catch (ClassroomNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/classrooms-dashboard";
        }
    }

    @PostMapping("/stuff/groups-dashboard/create")
    public String createGroup(@Valid @ModelAttribute("groupDTO") GroupDTO groupDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/groups-dashboard";
            }

            groupService.createGroup(groupDTO);

            String successMessage = "Group created successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/groups-dashboard";
        } catch (GroupException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/groups-dashboard";
        }
    }
    @PostMapping("/stuff/groups-dashboard/update")
    public String updateGroup(@Valid @ModelAttribute("groupDTO") GroupDTO groupDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/groups-dashboard";
            }

            groupService.updateGroup(groupDTO);

            String successMessage = "Group updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/groups-dashboard";
        } catch (GroupException | GroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/groups-dashboard";
        }
    }

    @GetMapping("/stuff/groups-dashboard/delete")
    public String removeGroupById(@RequestParam long groupId, RedirectAttributes redirectAttributes) {
        try {

            groupService.removeById(groupId);

            String successMessage = "Group deleted successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/groups-dashboard";
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Cannot delete group because it is associated with existing students.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/stuff/groups-dashboard";
        } catch (GroupException | GroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/groups-dashboard";
        }
    }

    @PostMapping("/stuff/students-dashboard/update")
    public String updateStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO , RedirectAttributes redirectAttributes) {
        try {

            studentService.assignStudentToGroup(studentDTO);

            String successMessage = "Student updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/students-dashboard";
        } catch (UserNotFoundException | GroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/students-dashboard";
        }
    }

    @PostMapping("/stuff/courses-dashboard/create")
    public String createCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/courses-dashboard";
            }

            courseService.createCourse(courseDTO);

            String successMessage = "Course created successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/courses-dashboard";
        } catch (UserNotFoundException | CourseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/courses-dashboard";
        }
    }
    @PostMapping("/stuff/courses-dashboard/update")
    public String updateCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/courses-dashboard";
            }

            courseService.updateCourse(courseDTO);

            String successMessage = "Course updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/courses-dashboard";
        } catch (UserNotFoundException | CourseException | CourseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/courses-dashboard";
        }
    }
    @GetMapping("/stuff/courses-dashboard/delete")
    public String removeCourseById(@RequestParam long courseId, RedirectAttributes redirectAttributes) {
        try {

            courseService.removeById(courseId);

            String successMessage = "Course deleted successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/courses-dashboard";
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Cannot delete course because it is associated with existing lessons.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/stuff/courses-dashboard";
        } catch (CourseException | CourseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/courses-dashboard";
        }
    }

    @PostMapping("/stuff/lessons-dashboard/create")
    public String createLesson(@Valid @ModelAttribute("lessonDTO") LessonDTO lessonDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/lessons-dashboard";
            }

            scheduleService.createLesson(lessonDTO);

            String successMessage = "Lesson created successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/lessons-dashboard";
        } catch (ClassroomNotFoundException | GroupNotFoundException | CourseNotFoundException | OverlapTimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/lessons-dashboard";
        }
    }
    @PostMapping("/stuff/lessons-dashboard/update")
    public String updateLesson(@Valid @ModelAttribute("courseDTO") LessonDTO lessonDTO , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage =  bindingResult
                        .getAllErrors()
                        .stream()
                        .findFirst().map(error -> error.getDefaultMessage()).get();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/stuff/lessons-dashboard";
            }

            scheduleService.updateLesson(lessonDTO);

            String successMessage = "Lesson updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/lessons-dashboard";
        } catch (LessonNotFoundException | ClassroomNotFoundException | GroupNotFoundException | CourseNotFoundException | OverlapTimeException  e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/lessons-dashboard";
        }
    }
    @GetMapping("/stuff/lessons-dashboard/delete")
    public String removeLessonById(@RequestParam long lessonId, RedirectAttributes redirectAttributes) {
        try {

            scheduleService.removeLessonById(lessonId);

            String successMessage = "Lesson deleted successfully!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/stuff/lessons-dashboard";
        } catch (LessonNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/stuff/lessons-dashboard";
        }
    }

}
