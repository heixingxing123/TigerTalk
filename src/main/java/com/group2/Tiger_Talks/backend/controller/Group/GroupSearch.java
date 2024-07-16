package com.group2.Tiger_Talks.backend.controller.Group;

import com.group2.Tiger_Talks.backend.model.Group.GroupDTO;
import com.group2.Tiger_Talks.backend.service.Group.GroupSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groupSearch")
public class GroupSearch {

    @Autowired
    private GroupSearchService groupSearchService;

    @GetMapping("/publicGroups/{groupName}/{userEmail}")
    public List<GroupDTO> findPublicGroupByName(@PathVariable String groupName, String userEmail) {
        return groupSearchService.findPublicGroupMatch(groupName,userEmail);
    }
}
