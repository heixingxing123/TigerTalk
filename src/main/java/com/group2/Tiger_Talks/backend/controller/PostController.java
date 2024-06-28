package com.group2.Tiger_Talks.backend.controller;

import com.group2.Tiger_Talks.backend.model.Post;
import com.group2.Tiger_Talks.backend.model.PostDTO;
import com.group2.Tiger_Talks.backend.model.UserProfile;
import com.group2.Tiger_Talks.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.group2.Tiger_Talks.backend.model.Utils.CROSS_ORIGIN_HOST_NAME;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @GetMapping("/getPostForUserAndFriends/{email}")
    public ResponseEntity<List<PostDTO>> getPostsForUserAndFriends(@PathVariable String email) {
        return ResponseEntity.ok(postService.getPostsForUserAndFriends(email));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("/getPostForUserAndFriends/userProfile")
    public ResponseEntity<List<PostDTO>> getPostsForUserAndFriendsByProfile(@RequestBody UserProfile userProfile) {
        return ResponseEntity.ok(postService.getPostsForUserAndFriends(userProfile));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @GetMapping("/getPostForUser/{email}")
    public ResponseEntity<List<PostDTO>> getPostsForUser(@PathVariable String email) {
        return ResponseEntity.ok(postService.getPostsForUser(email));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("/getPostForUser/userProfile")
    public ResponseEntity<List<PostDTO>> getPostsForUserByProfile(@RequestBody UserProfile userProfile) {
        return ResponseEntity.ok(postService.getPostsForUser(userProfile));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        return postService.createPost(post)
                .map(err -> ResponseEntity.badRequest().body(err))
                .orElseGet(() -> ResponseEntity.ok("Post created successfully."));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Integer postId) {
        return postService.deletePostById(postId)
                .map(err -> ResponseEntity.badRequest().body(err))
                .orElseGet(() -> ResponseEntity.ok("Post deleted successfully."));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestBody Post post) {
        return postService.deletePost(post)
                .map(err -> ResponseEntity.badRequest().body(err))
                .orElseGet(() -> ResponseEntity.ok("Post deleted successfully."));
    }


    @PutMapping("update/{postId}")
    public ResponseEntity<String> updatePostById(@PathVariable Integer postId, @RequestBody Post post) {
        Optional<String> postServiceOptional = postService.updatePostById(postId, post);
        return postServiceOptional.map(ResponseEntity::ok).orElseGet(()
                -> ResponseEntity.status(401).body("Failed to update post"));
    }

}
