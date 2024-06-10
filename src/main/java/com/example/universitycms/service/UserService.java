package com.example.universitycms.service;

import com.example.universitycms.model.User;
import com.example.universitycms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if (!userRepository.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("This user Id doesn't exist!");
        }

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
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

    public User getUserByUserId(long userId) {

        if(!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("This user Id doesn't exist!");
        }

        return userRepository.findUserByUserId(userId);
    }

    public User getUserByEmail(String email) {

        if(email == null) {
            throw new IllegalArgumentException("Input contains null!");
        }

        if(!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email doesn't exist!");
        }

        return userRepository.findUserByEmail(email);
    }


}
