package com.example.universitycms.controller;

import com.example.universitycms.model.User;
import com.example.universitycms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StuffStudentController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    private Logger logger = LoggerFactory.getLogger(StuffStudentController.class);


    @GetMapping("/stuff/students")
    public String showStuffStudentPage(Model model) {
        List<User> userList = userService.getUsersByRole(3);
        model.addAttribute("userList", userList);
        return "stuff-student-page";
    }

    @GetMapping("/stuff/students/assign-student-on-course/{userId}" )
    public String showStuffAssignStudentOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForTeacher(userId));
        return "stuff-assign-student-on-course-page";
    }

    @GetMapping("/stuff/students/remove-student-from-course/{userId}")
    public String showStuffRemoveStudentFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForTeacher(userId));
        return "stuff-remove-student-from-course-page";
    }

    @PostMapping("/stuff/students/assign-student-on-course/{userId}/{courseId}")
    public String assignStudentOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignTeacherOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";

    }
    @PostMapping("/stuff/students/remove-student-from-course/{userId}/{courseId}")
    public String removeStudentFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeTeacherFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";
    }


}
