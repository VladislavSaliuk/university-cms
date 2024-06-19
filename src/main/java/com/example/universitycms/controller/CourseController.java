package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("/admin/courses")
    public String showAdminCoursePage(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("courseList", courses);
        return "admin-course-page";
    }

    @GetMapping("/stuff/courses")
    public String showStuffCoursePage(Model model) {
        List<Course> courseList = courseService.getAll();
        model.addAttribute("courseList", courseList);
        return "stuff-course-page";
    }


    @GetMapping(value = {"/admin/courses/add-course", "/stuff/courses/add-course"})
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course-page";
    }

    @PostMapping(value = {"/admin/courses/add-course", "/stuff/courses/add-course"})
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes, Authentication authentication) {

        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/courses";
        } else {
            return "redirect:/stuff/courses";
        }
    }

    @GetMapping("/admin/courses/edit-course/{courseId}")
    public String showAdminEditCourseForm(@PathVariable long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        model.addAttribute("course", course);
        return "admin-edit-course-page";
    }

    @GetMapping("/stuff/courses/edit-course/{courseId}")
    public String showStuffEditCourseForm(@PathVariable long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        model.addAttribute("course", course);
        return "stuff-edit-course-page";
    }

    @PostMapping(value = {"/admin/courses/edit-course/{courseId}", "/stuff/courses/edit-course/{courseId}"})
    public String editCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/courses";
        } else {
            return "redirect:/stuff/courses";
        }

    }

    @GetMapping("/admin/courses/delete-course/{courseId}")
    public String deleteCourse(@PathVariable long courseId, RedirectAttributes redirectAttributes) {
        try {
            courseService.removeCourseByCourseId(courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }
        return "redirect:/admin/courses";
    }

}
