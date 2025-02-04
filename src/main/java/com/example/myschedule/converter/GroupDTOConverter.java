package com.example.myschedule.converter;

import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.exception.GroupException;
import com.example.myschedule.service.GroupService;
import org.springframework.core.convert.converter.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupDTOConverter implements Converter<String, GroupDTO> {

    private final GroupService groupService;
    @Override
    public GroupDTO convert(String source) {
        if (source == null || source.isEmpty()) {
            throw new GroupException("Group is empty!");
        }
        Long groupId = Long.parseLong(source);
        return groupService.getById(groupId);
    }

}
