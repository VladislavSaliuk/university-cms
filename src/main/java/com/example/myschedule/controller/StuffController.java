package com.example.myschedule.controller;

import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.dto.LessonDTO;
import com.example.myschedule.exception.ClassroomException;
import com.example.myschedule.exception.ClassroomNotFoundException;
import com.example.myschedule.exception.GroupException;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        } catch (ClassroomException | ClassroomNotFoundException e) {
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
        } catch (ClassroomException e) {
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

}
