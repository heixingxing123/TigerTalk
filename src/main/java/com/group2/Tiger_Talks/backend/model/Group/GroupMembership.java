package com.group2.Tiger_Talks.backend.model.Group;

import com.group2.Tiger_Talks.backend.model.User.UserProfile;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupMembershipId;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "email")
    private UserProfile member;

    private LocalDateTime joinTime = LocalDateTime.now();

    private boolean isCreator;


    public GroupMembership() {
    }

    public GroupMembership(Group group, UserProfile member, boolean isCreator) {
        this.group = group;
        this.member = member;
        this.isCreator = isCreator;
    }

    public int getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(int membershipId) {
        this.groupMembershipId = membershipId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UserProfile getMember() {
        return member;
    }

    public void setMember(UserProfile member) {
        this.member = member;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }


}