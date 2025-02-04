package com.example.myschedule.converter;

import com.example.myschedule.dto.CourseDTO;
import com.example.myschedule.exception.CourseException;
import com.example.myschedule.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDTOConverter implements Converter<String, CourseDTO> {

    private final CourseService courseService;
    @Override
    public CourseDTO convert(String source) {
        if (source == null || source.isEmpty()) {
            throw new CourseException("Course is empty!");
        }
        Long courseId = Long.parseLong(source);
        return courseService.getById(courseId);
    }
}
