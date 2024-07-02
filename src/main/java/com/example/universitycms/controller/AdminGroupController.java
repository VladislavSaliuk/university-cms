package com.example.universitycms.controller;

import com.example.universitycms.model.Group;
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

@Controller
public class AdminGroupController {

    @Autowired
    private GroupService groupService;
    private Logger logger = LoggerFactory.getLogger(AdminCourseController.class);

    @GetMapping("/admin/groups")
    public String showAdminGroupPage(Model model) {
        model.addAttribute("groupList", groupService.getAll());
        return "admin-group-page";
    }

    @GetMapping("/admin/groups/add-group")
    public String showAddGroupPage(Model model) {
        model.addAttribute("group", new Group());
        return "add-group-page";
    }

    @GetMapping("/admin/groups/edit-group/{groupId}")
    public String showAdminEditGroupPage(@PathVariable long groupId, Model model) {
        Group group = groupService.getGroupByGroupId(groupId);
        model.addAttribute("group", group);
        return "admin-edit-group-page";
    }

    @PostMapping("/admin/groups/add-group")
    public String addGroup(@ModelAttribute Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.createGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", "Group added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/admin/groups";
    }

    @PostMapping("/admin/groups/edit-group/{groupId}")
    public String editGroup(@ModelAttribute Group group, RedirectAttributes redirectAttributes) {
        try {
            groupService.updateGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", "Group updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/admin/groups";
    }

    @GetMapping("/admin/groups/delete-group/{groupId}")
    public String deleteGroup(@PathVariable long groupId, RedirectAttributes redirectAttributes) {
        try {
            groupService.removeGroupByGroupId(groupId);
            redirectAttributes.addFlashAttribute("successMessage", "Group deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }
        return "redirect:/admin/groups";
    }

}
