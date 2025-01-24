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
public class TeacherController {

    private final ScheduleService scheduleService;
    @GetMapping("/teacher")
    public String showTeacherHomePage() {
        return "teacher-home-page";
    }
    @GetMapping("/teacher/schedule")
    public String showTeacherSchedulePage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            long userId = (long) session.getAttribute("userId");
            List<LessonDTO> lessonList = scheduleService.getScheduleForTeacher(userId);
            model.addAttribute("lessonList", lessonList);
            return "teacher-schedule-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

}
