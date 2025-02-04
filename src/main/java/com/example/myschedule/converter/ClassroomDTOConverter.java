package com.example.myschedule.converter;

import com.example.myschedule.dto.ClassroomDTO;
import com.example.myschedule.exception.ClassroomException;
import com.example.myschedule.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassroomDTOConverter implements Converter<String, ClassroomDTO> {

    private final ClassroomService classroomService;
    @Override
    public ClassroomDTO convert(String source) {
        if (source == null || source.isEmpty()) {
            throw new ClassroomException("Classroom is empty!");
        }
        Long classroomId = Long.parseLong(source);
        return classroomService.getById(classroomId);
    }
}
