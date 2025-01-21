package com.example.myschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/about-me")
    public String showAboutMePage() {
        return "about-me-page";
    }

    @GetMapping("/features")
    public String showFeaturesPage() {
        return "features-page";
    }

    @GetMapping("/contact-me")
    public String showContactMePage() {
        return "contact-me-page";
    }

}