package com.example.myschedule.controller;

import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.dto.LessonDTO;
import com.example.myschedule.entity.Lesson;
import com.example.myschedule.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String showStuffStudentsDashboardPage(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("studentList", studentService.getAllStudents());
        return "stuff-students-dashboard-page";
    }
    @GetMapping("/stuff/teachers-dashboard")
    public String showStuffTeachersDashboardPage(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("teacherList", teacherService.getAllTeachers());
        return "stuff-teachers-dashboard-page";
    }

}
