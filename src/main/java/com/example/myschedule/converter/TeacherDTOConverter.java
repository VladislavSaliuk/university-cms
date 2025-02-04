package com.example.myschedule.converter;

import com.example.myschedule.dto.TeacherDTO;
import com.example.myschedule.dto.UserDTO;
import com.example.myschedule.exception.GroupException;
import com.example.myschedule.exception.UserException;
import com.example.myschedule.repository.UserRepository;
import com.example.myschedule.service.UserService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@Component
@RequiredArgsConstructor
public class TeacherDTOConverter implements Converter<String, TeacherDTO>{

    private final UserService userService;
    @Override
    public TeacherDTO convert(String source) {
        if (source == null || source.isEmpty()) {
            throw new UserException("User is empty!");
        }
        Long userId = Long.parseLong(source);

        UserDTO userDTO = userService.getById(userId);

        TeacherDTO teacherDTO = TeacherDTO.builder()
                .userId(userDTO.getUserId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .status(userDTO.getStatus())
                .role(userDTO.getRole())
                .build();

        return teacherDTO;
    }
}
