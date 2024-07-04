package com.example.universitycms.service;

import com.example.universitycms.controller.StudentTeacherController;
import com.example.universitycms.model.Course;
import com.example.universitycms.model.Role;
import com.example.universitycms.model.User;
import com.example.universitycms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user) {

        if(user == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(userRepository.existsByUserName(user.getUserName())) {
            throw new IllegalArgumentException("This username is already exists!");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("This E-mail is already exists!");
        }

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(long roleId) {

        if(!userRepository.existsByRole_RoleId(roleId)) {
            throw new IllegalArgumentException("This role doesn't exist!");
        }

        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().getRoleId() == roleId)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if(user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRole().getRoleName())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }


    public User getUserByUsername(String username) {

        User user = userRepository.findUserByUserName(username);

        if(user == null) {
            throw new IllegalArgumentException("User with this name doesn't exist!");
        }

        return user;
    }

    public List<User> getTeachersByUserId(long userId) {

        User user = userRepository.findUserByUserId(userId);

        if(user == null) {
            throw new IllegalArgumentException("User with this Id doesn't exist!");
        }

        if(user.getRole().getRoleId() != 3) {
            throw new IllegalArgumentException("Your user is not a student!");
        }

        return user.getGroup().getUserSet()
                .stream().filter(teacher -> teacher.getRole().getRoleId() == 2)
                .collect(Collectors.toList());
    }

    public List<Course> getAllCoursesByUserId(long userId) {

        User user = userRepository.findUserByUserId(userId);

        if(user == null) {
            throw new IllegalArgumentException("User with this Id doesn't exist!");
        }

        return Stream.concat(user.getCourseList().stream(), user.getGroup().getCourseList().stream())
                .sorted((c1, c2) -> c1.getCourseName().compareTo(c2.getCourseName()))
                .collect(Collectors.toList());

    }


}
