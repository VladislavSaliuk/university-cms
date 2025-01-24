package com.example.myschedule.controller;

import com.example.myschedule.dto.LessonDTO;
import com.example.myschedule.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final ScheduleService scheduleService;
    @GetMapping("/student")
    public String showStudentHomePage() {
        return "student-home-page";
    }
    @GetMapping("/student/schedule")
    public String showStudentSchedulePage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            long userId = (long) session.getAttribute("userId");
            List<LessonDTO> lessonList = scheduleService.getScheduleForStudent(userId);
            model.addAttribute("lessonList", lessonList);
            return "student-schedule-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

}