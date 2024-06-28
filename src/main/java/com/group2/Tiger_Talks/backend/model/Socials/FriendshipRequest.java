package com.group2.Tiger_Talks.backend.model.Socials;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group2.Tiger_Talks.backend.model.UserProfile;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "friendship_request")
public class FriendshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendshipRequestId;

    @ManyToOne
    @JoinColumn(name = "sender_email", referencedColumnName = "email")
    @JsonBackReference("sender-friendship-request")
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email")
    @JsonBackReference("receiver-friendship-request")
    private UserProfile receiver;


    private LocalDate createTime = LocalDate.now();              // yyyy/mm/dd/00:00:00


    public FriendshipRequest(UserProfile sender,
                             UserProfile receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public FriendshipRequest() {
    }


    public Integer getFriendshipRequestId() {
        return friendshipRequestId;
    }

    public void setFriendshipRequestId(Integer friendshipRequestId) {
        this.friendshipRequestId = friendshipRequestId;
    }

    public UserProfile getSender() {
        return sender;
    }

    public void setSender(UserProfile userFriendshipSender) {
        this.sender = userFriendshipSender;
    }

    public UserProfile getReceiver() {
        return receiver;
    }

    public void setReceiver(UserProfile userFriendshipReceiver) {
        this.receiver = userFriendshipReceiver;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

}
