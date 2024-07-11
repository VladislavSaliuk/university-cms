package com.example.universitycms.controller;

import com.example.universitycms.model.User;
import com.example.universitycms.service.UserCourseService;
import com.example.universitycms.service.UserService;
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
public class StuffTeacherController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;
    private Logger logger = LoggerFactory.getLogger(StuffTeacherController.class);

    @GetMapping("/stuff/teachers")
    public String showStuffTeacherPage(Model model) {
        List<User> userList = userService.getUsersByRole(2);
        model.addAttribute("userList", userList);
        return "stuff-teacher-page";
    }

    @GetMapping("/stuff/teachers/assign-teacher-on-course/{userId}")
    public String showAssignTeacherOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForTeacher(userId));
        return "stuff-assign-teacher-on-course-page";
    }
    @GetMapping("/stuff/teachers/remove-teacher-from-course/{userId}")
    public String showStuffRemoveTeacherFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForTeacher(userId));
        return "stuff-remove-teacher-from-course-page";
    }

    @PostMapping( "/stuff/teachers/assign-teacher-on-course/{userId}/{courseId}")
    public String assignTeacherOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignTeacherOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";

    }
    @PostMapping("/stuff/teachers/remove-teacher-from-course/{userId}/{courseId}")
    public String removeTeacherFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeTeacherFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";
    }

}
