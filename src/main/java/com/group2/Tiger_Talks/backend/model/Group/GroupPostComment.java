package com.group2.Tiger_Talks.backend.model.Group;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GroupPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupPostCommentId;

    @ManyToOne
    @JoinColumn(name = "group_membership_id", referencedColumnName = "groupMembershipId", nullable = false)
    private GroupMembership groupMembership;

    @ManyToOne
    @JoinColumn(name = "group_post_id", referencedColumnName = "groupPostId", nullable = false)
    private GroupPost groupPost;

    private String content;

    @Column(nullable = false)
    private LocalDateTime groupPostCommentCreateTime = LocalDateTime.now();

    public GroupPostComment() {}

    public Integer getGroupPostCommentId() {
        return groupPostCommentId;
    }

    public void setGroupPostCommentId(Integer groupPostCommentId) {
        this.groupPostCommentId = groupPostCommentId;
    }

    public GroupMembership getGroupMembership() {
        return groupMembership;
    }

    public void setGroupMembership(GroupMembership groupMembership) {
        this.groupMembership = groupMembership;
    }

    public GroupPost getGroupPost() {
        return groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getGroupPostCommentCreateTime() {
        return groupPostCommentCreateTime;
    }

    public void setGroupPostCommentCreateTime(LocalDateTime groupPostCommentCreateTime) {
        this.groupPostCommentCreateTime = groupPostCommentCreateTime;
    }


}
