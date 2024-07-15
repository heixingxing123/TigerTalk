package com.group2.Tiger_Talks.backend.controller.Group;

import com.group2.Tiger_Talks.backend.model.Group.GroupPost;
import com.group2.Tiger_Talks.backend.model.Group.GroupPostDTO;
import com.group2.Tiger_Talks.backend.service.Group.GroupPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups/post")
public class GroupPostController {

    @Autowired
    private GroupPostService groupPostService;

    @PostMapping("/create")
    public ResponseEntity<String> createGroupPost(@RequestBody GroupPost groupPost) {
        Optional<String> result = groupPostService.createGroupPost(groupPost);
        if (result.isEmpty()) {
            return ResponseEntity.ok("Group post created successfully.");
        } else {
            return ResponseEntity.badRequest().body(result.get());
        }
    }

    @DeleteMapping("/delete/{groupPostId}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable Integer groupPostId) {
        Optional<String> result = groupPostService.deleteGroupPostById(groupPostId);
        if (result.isEmpty()) {
            return ResponseEntity.ok("Group post deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body(result.get());
        }
    }

    @GetMapping("/getAll/{groupId}")
    public List<GroupPostDTO> getAllGroupPostsByGroupId(@PathVariable Integer groupId) {
        return groupPostService.getAllGroupPostsByGroupId(groupId);

    }
}
