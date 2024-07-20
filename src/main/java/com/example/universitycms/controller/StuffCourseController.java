package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StuffCourseController {

    @Autowired
    private CourseService courseService;
    private Logger logger = LoggerFactory.getLogger(StuffCourseController.class);

    @GetMapping("/stuff/courses")
    public String showStuffCoursePage(Model model) {
        List<Course> courseList = courseService.getAll();
        model.addAttribute("courseList", courseList);
        return "stuff-course-page";
    }
    @GetMapping( "/stuff/courses/add-course")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course-page";
    }

    @GetMapping("/stuff/courses/edit-course/{courseId}")
    public String showStuffEditCourseForm(@PathVariable long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        model.addAttribute("course", course);
        return "stuff-edit-course-page";
    }
    @PostMapping("/stuff/courses/add-course")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {

        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/courses";
    }

    @PostMapping( "/stuff/courses/edit-course/{courseId}")
    public String editCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/courses";
    }

}
