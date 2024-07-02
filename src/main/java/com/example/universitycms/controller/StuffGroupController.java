package com.example.universitycms.controller;

import com.example.universitycms.model.Group;
import com.example.universitycms.service.GroupCourseService;
import com.example.universitycms.service.GroupService;
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
public class StuffGroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupCourseService groupCourseService;
    private Logger logger = LoggerFactory.getLogger(StuffGroupController.class);
    @GetMapping("/stuff/groups")
    public String showStuffGroupPage(Model model) {
        List<Group> groupList = groupService.getAll();
        model.addAttribute("groupList", groupList);
        return "stuff-group-page";
    }
    @GetMapping("/stuff/groups/add-group")
    public String showAddGroupPage(Model model) {
        model.addAttribute("group", new Group());
        return "add-group-page";
    }
    @GetMapping("/stuff/groups/edit-group/{groupId}")
    public String showStuffEditGroupPage(@PathVariable long groupId, Model model) {
        Group group = groupService.getGroupByGroupId(groupId);
        model.addAttribute("group", group);
        return "stuff-edit-group-page";
    }
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

    @GetMapping("/stuff/groups/assign-user-on-group/{groupId}")
    public String showAssignUserOnGroupPage(@PathVariable long groupId, Model model) {
        model.addAttribute("userList", groupService.getUnassignedUsersToGroup(groupId));
        return "stuff-assign-user-on-group-page";
    }

    @GetMapping("/stuff/groups/remove-user-from-group/{groupId}")
    public String showRemoveUserFromGroupPage(@PathVariable long groupId, Model model) {
        model.addAttribute("userList", groupService.getAssignedUsersToGroup(groupId));
        return "stuff-remove-user-from-group-page";
    }
    @PostMapping("/stuff/groups/add-group")
    public String addGroup(@ModelAttribute Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.createGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", "Group added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/groups";
    }
    @PostMapping("/stuff/groups/edit-group/{groupId}")
    public String editGroup(@ModelAttribute Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.updateGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", "Group updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/groups";
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
    @PostMapping("/stuff/groups/assign-user-on-group/{groupId}/{userId}")
    public String assignUserOnGroup(@PathVariable long groupId, @PathVariable long userId, RedirectAttributes redirectAttributes) {

        try {
            groupService.assignUserToGroup(groupId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "User assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/groups";

    }
    @PostMapping("/stuff/groups/remove-user-from-group/{groupId}/{userId}")
    public String removeUserFromGroup(@PathVariable long groupId, @PathVariable long userId, RedirectAttributes redirectAttributes) {

        try {
            groupService.removeUserFromGroup(groupId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "User removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/groups";
    }
}
