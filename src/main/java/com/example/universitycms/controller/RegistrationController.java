package com.example.universitycms.controller;

import com.example.universitycms.model.Student;
import com.example.universitycms.model.Teacher;
import com.example.universitycms.service.StudentService;
import com.example.universitycms.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/select-role")
public class RegistrationController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showSelectRolePage() {
        return "select-role-page";
    }

    @GetMapping("/register-teacher")
    public String showTeacherRegistrationPage(@ModelAttribute Teacher teacher, Model model) {
        model.addAttribute("teacher", teacher);
        return "teacher-registration-page";
    }

    @PostMapping("/register-teacher")
    public String registerTeacher(@ModelAttribute Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherService.addTeacher(teacher);
        return "redirect:/login";
    }

    @GetMapping("/register-student")
    public String showStudentRegistrationPage(@ModelAttribute Student student, Model model) {
        model.addAttribute("student", student);
        return "student-registration-page";
    }

    @PostMapping("/register-student")
    public String registerStudent(@ModelAttribute Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentService.addStudent(student);
        return "redirect:/login";
    }
}
