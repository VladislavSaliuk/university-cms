package com.example.universitycms.controller;


import com.example.universitycms.service.GroupCourseService;
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
public class GroupCourseController {


    @Autowired
    private GroupCourseService groupCourseService;

    private Logger logger = LoggerFactory.getLogger(GroupCourseController.class);

    @GetMapping("/stuff/groups/assign-course-on-group/{groupId}")
    public String showStuffAssignCourseOnGroupPage(@PathVariable long groupId, Model model) {
        model.addAttribute("courseList", groupCourseService.getUnassignedCoursesForGroup(groupId));
        return "stuff-assign-course-on-group-page";
    }

    @GetMapping("/stuff/groups/remove-course-from-group/{groupId}")
    public String showStuffRemoveCourseFromGroup(@PathVariable long groupId, Model model) {
        model.addAttribute("courseList", groupCourseService.getAssignedCoursesForGroup(groupId));
        return "stuff-remove-course-from-group-page";
    }

    @PostMapping("/stuff/groups/assign-course-on-group/{groupId}/{courseId}")
    public String assignCourseOnGroup(@PathVariable long groupId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            groupCourseService.assignCourseOnGroup(groupId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/groups";

    }

    @PostMapping("/stuff/groups/remove-course-from-group/{groupId}/{courseId}")
    public String removeCourseFromGroup(@PathVariable long groupId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            groupCourseService.removeCourseFromGroup(groupId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/groups";
    }

}
