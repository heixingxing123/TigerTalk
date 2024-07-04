package com.group2.Tiger_Talks.backend.model.Post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group2.Tiger_Talks.backend.model.Post.Post;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "postId")
    @JsonBackReference
    private Post post;

    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public PostComment() {
    }

    public PostComment(Post post, String content) {
        this.post = post;
        this.content = content;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}