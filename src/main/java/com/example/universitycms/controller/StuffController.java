package com.example.universitycms.controller;

import com.example.universitycms.model.Course;
import com.example.universitycms.model.Group;
import com.example.universitycms.model.User;
import com.example.universitycms.service.*;
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
public class StuffController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private GroupCourseService groupCourseService;

    private Logger logger = LoggerFactory.getLogger(StuffController.class);

    @GetMapping("/stuff/groups")
    public String showStuffGroupPage(Model model) {
        List<Group> groupList = groupService.getAll();
        model.addAttribute("groupList", groupList);
        return "stuff-group-page";
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

    @GetMapping("/stuff/courses")
    public String showStuffCoursePage(Model model) {
        List<Course> courseList = courseService.getAll();
        model.addAttribute("courseList", courseList);
        return "stuff-course-page";
    }

    @GetMapping( "/stuff/courses/add-course")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course-page";
    }
    @PostMapping("/stuff/courses/add-course")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {

        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/courses";
    }

    @GetMapping("/stuff/courses/edit-course/{courseId}")
    public String showStuffEditCourseForm(@PathVariable long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        model.addAttribute("course", course);
        return "stuff-edit-course-page";
    }

    @PostMapping( "/stuff/courses/edit-course/{courseId}")
    public String editCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }

        return "redirect:/stuff/courses";
    }
    @GetMapping("/stuff/teachers")
    public String showStuffTeacherPage(Model model) {
        List<User> userList = userService.getUsersByRole(2);
        model.addAttribute("userList", userList);
        return "stuff-teacher-page";
    }
    @GetMapping("/stuff/teachers/assign-teacher-on-course/{userId}")
    public String showAssignTeacherOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForUser(userId));
        return "stuff-assign-teacher-on-course-page";
    }
    @GetMapping("/stuff/teachers/remove-teacher-from-course/{userId}")
    public String showStuffRemoveTeacherFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForUser(userId));
        return "stuff-remove-teacher-from-course-page";
    }

    @PostMapping( "/stuff/teachers/assign-teacher-on-course/{userId}/{courseId}")
    public String assignTeacherOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignUserOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";

    }
    @GetMapping("/stuff/students")
    public String showStuffStudentPage(Model model) {
        List<User> userList = userService.getUsersByRole(3);
        model.addAttribute("userList", userList);
        return "stuff-student-page";
    }

    @GetMapping("/stuff/students/assign-student-on-course/{userId}" )
    public String showStuffAssignStudentOnCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getUnassignedCoursesForUser(userId));
        return "stuff-assign-student-on-course-page";
    }

    @GetMapping("/stuff/students/remove-student-from-course/{userId}")
    public String showStuffRemoveStudentFromCoursePage(@PathVariable long userId, Model model) {
        model.addAttribute("courseList", userCourseService.getAssignedCoursesForUser(userId));
        return "stuff-remove-student-from-course-page";
    }

    @PostMapping("/stuff/students/assign-student-on-course/{userId}/{courseId}")
    public String assignStudentOnCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.assignUserOnCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course assigned successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";

    }
    @PostMapping("/stuff/teachers/remove-teacher-from-course/{userId}/{courseId}")
    public String removeTeacherFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeUserFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/teachers";
    }
    @PostMapping("/stuff/students/remove-student-from-course/{userId}/{courseId}")
    public String removeStudentFromCourse(@PathVariable long userId, @PathVariable long courseId, RedirectAttributes redirectAttributes) {

        try {
            userCourseService.removeUserFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully!");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.error(exception.getMessage());
        }

        return "redirect:/stuff/students";
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

    @GetMapping("/stuff/groups/add-group")
    public String showAddGroupPage(Model model) {
        model.addAttribute("group", new Group());
        return "add-group-page";
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

    @GetMapping("/stuff/groups/edit-group/{groupId}")
    public String showStuffEditGroupPage(@PathVariable long groupId, Model model) {
        Group group = groupService.getGroupByGroupId(groupId);
        model.addAttribute("group", group);
        return "stuff-edit-group-page";
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

}
