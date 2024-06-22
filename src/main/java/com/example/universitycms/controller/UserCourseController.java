package com.example.universitycms.controller;

import com.example.universitycms.service.UserCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserCourseController {


    @Autowired
    private UserCourseService userCourseService;

    private Logger logger = LoggerFactory.getLogger(GroupCourseController.class);

    @GetMapping("/stuff/teachers/assign-teacher-on-course/{userId}")
    public String showAssignTeacherOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForUser(userId));
        return "stuff-assign-teacher-on-course-page";
    }

    @GetMapping("/stuff/students/assign-student-on-course/{userId}" )
    public String showAssignStudentOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForUser(userId));
        return "stuff-assign-student-on-course-page";
    }

    @GetMapping("/stuff/teachers/remove-teacher-from-course/{userId}")
    public String showStuffRemoveTeacherFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForUser(userId));
        return "stuff-remove-teacher-from-course-page";
    }

    @GetMapping("/stuff/students/remove-student-from-course/{userId}")
    public String showStuffRemoveStudentFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForUser(userId));
        return "stuff-remove-student-from-course-page";
    }

    @PostMapping( "/stuff/teachers/assign-teacher-on-course/{userId}/{courseId}")
    public String assignTeacherOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignUserOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";

    }
    @PostMapping("/stuff/students/assign-student-on-course/{userId}/{courseId}")
    public String assignStudentOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignUserOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";

    }
    @PostMapping("/stuff/teachers/remove-teacher-from-course/{userId}/{courseId}")
    public String removeTeacherFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeUserFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";
    }
    @PostMapping("/stuff/students/remove-student-from-course/{userId}/{courseId}")
    public String removeStudentFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeUserFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";
    }


}