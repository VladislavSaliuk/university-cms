package com.example.myschedule.service;

import com.example.myschedule.dto.GroupDTO;
import com.example.myschedule.entity.Group;
import com.example.myschedule.exception.GroupException;
import com.example.myschedule.exception.GroupNotFoundException;
import com.example.myschedule.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupDTO createGroup(GroupDTO groupDTO) {
        log.info("Attempting to create a group with name: {}", groupDTO.getName());

        if (groupRepository.existsByName(groupDTO.getName())) {
            log.warn("Group with name {} already exists!", groupDTO.getName());
            throw new GroupException("Group with " + groupDTO.getName() + " name already exists!");
        }

        Group group = groupRepository.save(Group.toGroup(groupDTO));
        log.info("Successfully created group with ID: {} and name: {}", group.getGroupId(), group.getName());

        return GroupDTO.toGroupDTO(group);
    }

    @Transactional
    public GroupDTO updateGroup(GroupDTO groupDTO) {
        log.info("Attempting to update group with ID: {}", groupDTO.getGroupId());

        Group updatedGroup = groupRepository.findById(groupDTO.getGroupId())
                .orElseThrow(() -> {
                    log.error("Group with ID {} not found!", groupDTO.getGroupId());
                    return new GroupNotFoundException("Group with " + groupDTO.getGroupId() + " Id not found!");
                });

        if (groupRepository.existsByName(groupDTO.getName())) {
            log.warn("Group with name {} already exists!", groupDTO.getName());
            throw new GroupException("Group with " + groupDTO.getName() + " name already exists!");
        }

        updatedGroup.setName(groupDTO.getName());
        log.info("Successfully updated group with ID: {} to name: {}", updatedGroup.getGroupId(), updatedGroup.getName());

        return GroupDTO.toGroupDTO(updatedGroup);
    }

    public List<GroupDTO> getAll() {
        log.info("Fetching all groups");

        List<GroupDTO> groups = groupRepository.findAll()
                .stream()
                .map(GroupDTO::toGroupDTO)
                .collect(Collectors.toList());

        log.info("Fetched {} groups", groups.size());
        return groups;
    }

    public GroupDTO getById(long groupId) {
        log.info("Fetching group with ID: {}", groupId);

        return groupRepository.findById(groupId)
                .map(group -> {
                    log.info("Group found with ID: {}", groupId);
                    return GroupDTO.toGroupDTO(group);
                })
                .orElseThrow(() -> {
                    log.error("Group with ID {} not found!", groupId);
                    return new GroupNotFoundException("Group with " + groupId + " Id not found!");
                });
    }

    public GroupDTO getByName(String name) {
        log.info("Fetching group with name: {}", name);

        return groupRepository.findByName(name)
                .map(group -> {
                    log.info("Group found with name: {}", name);
                    return GroupDTO.toGroupDTO(group);
                })
                .orElseThrow(() -> {
                    log.error("Group with name {} not found!", name);
                    return new GroupNotFoundException("Group with " + name + " name not found!");
                });
    }

    public void removeById(long groupId) {
        log.info("Attempting to remove group with ID: {}", groupId);

        if (!groupRepository.existsById(groupId)) {
            log.warn("Group with ID {} does not exist!", groupId);
            throw new GroupNotFoundException("Group with " + groupId + " Id not found!");
        }

        groupRepository.deleteById(groupId);
        log.info("Successfully removed group with ID: {}", groupId);
    }

    

}