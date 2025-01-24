package com.example.myschedule.controller;

import com.example.myschedule.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "stuff-classrooms-dashboard-page";
    }

    @GetMapping("/stuff/courses-dashboard")
    public String showStuffCoursesDashboardPage(Model model) {
        model.addAttribute("courseList", courseService.getAll());
        return "stuff-courses-dashboard-page";
    }

    @GetMapping("/stuff/groups-dashboard")
    public String showStuffGroupsDashboardPage(Model model) {
        model.addAttribute("groupList", groupService.getAll());
        return "stuff-groups-dashboard-page";
    }

    @GetMapping("/stuff/lessons-dashboard")
    public String showStuffLessonsDashboardPage(Model model) {
        model.addAttribute("lessonList", scheduleService.getAllLessons());
        return "stuff-lessons-dashboard-page";
    }

    @GetMapping("/stuff/students-dashboard")
    public String showStuffStudentsDashboardPage(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("studentList", studentService.getAllStudents());
            return "stuff-students-dashboard-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }
    @GetMapping("/stuff/teachers-dashboard")
    public String showStuffTeachersDashboardPage(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("teacherList", teacherService.getAllTeachers());
            return "stuff-teachers-dashboard-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

}
