package com.example.universitycms.controller;

import com.example.universitycms.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups")
    public String showGroupPage(Model model) {
        model.addAttribute("groupList", groupService.getAll());
        return "groups_page";
    }

}
