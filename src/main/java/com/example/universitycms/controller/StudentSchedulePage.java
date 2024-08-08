package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class StudentSchedulePage {

    @Autowired
    private CourseService courseService;

    @GetMapping("/student/schedule")
    public String showStudentSchedulePage(Model model) {
        List<Course> courses = courseService.getAll();
        Map<String, List<Course>> schedule = new LinkedHashMap<>();

        // Initialize days of the week
        schedule.put("Monday", new ArrayList<>());
        schedule.put("Tuesday", new ArrayList<>());
        schedule.put("Wednesday", new ArrayList<>());
        schedule.put("Thursday", new ArrayList<>());
        schedule.put("Friday", new ArrayList<>());
        schedule.put("Saturday", new ArrayList<>());

        // Populate the schedule
        for (Course course : courses) {
            String day = course.getDayOfWeek();
            if (day != null && schedule.containsKey(day)) {
                schedule.get(day).add(course);
            }
        }

        model.addAttribute("schedule", schedule);
        return "student-schedule-page";
    }
}