package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/admin/courses")
    public String showAdminCoursePageView(Model model) {
        model.addAttribute("courseList",courseService.getAll());
        return "admin-course-page";
    }

    @GetMapping("/admin/courses/add-course")
    public String showAddCoursePageView(@ModelAttribute Course course, Model model) {
        model.addAttribute("course", course);
        return "add-course-page";
    }

    @GetMapping("/admin/courses/edit-course/{courseId}")
    public String showEditCoursePageView(@PathVariable long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        model.addAttribute("course", course);
        return "edit-course-page";
    }

    @PostMapping("/admin/courses/add-course")
    public String createCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/courses/delete-course/{courseId}")
    public String deleteCourse(@PathVariable long courseId) {
        courseService.removeCourseByCourseId(courseId);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/edit-course/{courseId}")
    public String editCourse(@ModelAttribute Course course) {
        courseService.updateCourse(course);
        return "redirect:/admin/courses";
    }


}