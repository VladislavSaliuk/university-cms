package com.example.universitycms.controller;

import com.example.universitycms.model.Group;
import com.example.universitycms.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/stuff/groups")
    public String showStuffGroupPage(Model model) {
        List<Group> groupList = groupService.getAll();
        model.addAttribute("groupList", groupList);
        return "stuff-group-page";
    }

}
