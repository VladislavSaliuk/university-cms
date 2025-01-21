package com.example.myschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StuffController {

    @GetMapping("/stuff")
    public String showStuffHomePage() {
        return "stuff-home-page";
    }

}
