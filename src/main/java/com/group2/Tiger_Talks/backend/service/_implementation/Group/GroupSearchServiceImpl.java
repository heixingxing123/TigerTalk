package com.group2.Tiger_Talks.backend.service._implementation.Group;

import com.group2.Tiger_Talks.backend.model.Group.GroupDTO;
import com.group2.Tiger_Talks.backend.repository.Group.GroupRepository;
import com.group2.Tiger_Talks.backend.service.Group.GroupSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSearchServiceImpl implements GroupSearchService {
    @Autowired
    private GroupRepository groupRepository;


    @Override
    public List<GroupDTO> findPublicGroupMatch(String groupName) {
        return groupRepository.findAll()
                .stream()
                .filter(group -> group.getGroupName().startsWith(groupName) && !group.isPrivate())
                .map(GroupDTO::new)
                .toList();
    }
}
